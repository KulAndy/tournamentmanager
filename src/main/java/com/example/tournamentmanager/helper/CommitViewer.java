package com.example.tournamentmanager.helper;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.tournamentmanager.helper.GeneralHelper.error;
import static com.example.tournamentmanager.helper.GeneralHelper.info;

public class CommitViewer {

    private final File repo = new File(".", ".git");
    private ListView<String> commitListView;
    private Path tempDir;

    public void displayCommitViewer() {
        if (!repo.exists()) {
            error("Couldn't check version history");
            return;
        }

        try {
            tempDir = Files.createTempDirectory("tempGitRepo");
        } catch (IOException e) {
            error("Failed to create a temporary directory");
            return;
        }

        commitListView = new ListView<>();

        CompletableFuture.runAsync(this::fetchUpdatesIntoTempDir)
                .thenRunAsync(this::displayCommits, Platform::runLater)
                .thenRunAsync(this::showResultWindow, Platform::runLater);
    }

    private void fetchUpdatesIntoTempDir() {
        try (Repository repository = new RepositoryBuilder().setGitDir(repo).build();
             Git git = Git.cloneRepository()
                     .setURI(repository.getConfig().getString("remote", "origin", "url"))
                     .setDirectory(tempDir.toFile())
                     .call()) {
            git.fetch().call();
        } catch (IOException | GitAPIException e) {
            error("Couldn't fetch updates into temporary directory");
        }
    }

    private void displayCommits() {
        try (Repository repository = new RepositoryBuilder().setGitDir(tempDir.resolve(".git").toFile()).build();
             Git git = new Git(repository)) {

            ObjectId currentCommitId = repository.resolve("HEAD");

            if (currentCommitId != null) {
                List<String> newerCommits = new ArrayList<>();

                Iterable<RevCommit> remoteCommits = git.log().call();
                for (RevCommit commit : remoteCommits) {
                    if (commit.getId().equals(currentCommitId)) {
                        break;
                    }
                    newerCommits.add(commit.getShortMessage());
                }

                commitListView.getItems().addAll(newerCommits);

            }
        } catch (IOException | GitAPIException e) {
            error("Couldn't display commits");
        }
    }

    private void showResultWindow() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Commits");
        dialog.setHeaderText(null);

        VBox vBox = new VBox(10);

        Button mergeButton = new Button("Update to selected version");
        mergeButton.setOnAction(e -> {
            mergeSelectedCommits();
            dialog.close();
        });

        vBox.getChildren().addAll(commitListView, mergeButton);

        dialog.getDialogPane().setContent(vBox);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButton) {
                removeTempDir();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void mergeSelectedCommits() {
        String selectedCommitMessage = commitListView.getSelectionModel().getSelectedItem();

        if (selectedCommitMessage != null) {
            try (Repository repository = new RepositoryBuilder().setGitDir(repo).build();
                 Git git = new Git(repository)) {

                git.clean().setCleanDirectories(true).call();

                try (Repository tempRepo = new RepositoryBuilder().setGitDir(tempDir.resolve(".git").toFile()).build();
                     Git tempGit = new Git(tempRepo)) {

                    Iterable<RevCommit> commits = tempGit.log().call();
                    String selectedCommitId = null;

                    for (RevCommit commit : commits) {
                        if (commit.getShortMessage().equals(selectedCommitMessage)) {
                            selectedCommitId = commit.getName();
                            break;
                        }
                    }

                    if (selectedCommitId != null) {
                        tempGit.reset().setRef(selectedCommitId).setMode(ResetCommand.ResetType.HARD).call();

                        copyFiles(tempDir, Path.of("."));

                        MergeResult mergeResult = git.merge().include(tempRepo.resolve(selectedCommitId)).call();

                        if (mergeResult.getMergeStatus().isSuccessful()) {
                            info("Update successful!");
                        } else {
                            error("Update failed: " + mergeResult.getMergeStatus().toString());
                        }
                    } else {
                        error("Invalid selected commit: " + selectedCommitMessage);
                    }
                }
            } catch (IOException | GitAPIException e) {
                error("Update failed");
                e.printStackTrace();
            }
        }
    }

    private void copyFiles(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = source.relativize(file);
                Path destinationFile = destination.resolve(relativePath);
                Files.copy(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void removeTempDir() {
        try {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ignored) {
        }
    }
}
