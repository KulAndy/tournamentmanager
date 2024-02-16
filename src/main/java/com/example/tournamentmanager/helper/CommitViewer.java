package com.example.tournamentmanager.helper;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.tournamentmanager.helper.DialogHelper.error;
import static com.example.tournamentmanager.helper.DialogHelper.info;

public class CommitViewer {

    private final File repo = new File(".", ".git");
    private ListView<String> commitListView;
    private Path tempDir;

    public void displayCommitViewer() {
        if (!repo.exists()) {
            error("Couldn't check version history");
            return;
        }

        commitListView = new ListView<>();
        commitListView.setMouseTransparent(true);
        commitListView.setFocusTraversable(false);

        Task<Void> fetchAndDisplayTask = getFetchAndDisplayTask();

        CompletableFuture.runAsync(fetchAndDisplayTask);
    }

    @NotNull
    private Task<Void> getFetchAndDisplayTask() {
        Task<Void> fetchAndDisplayTask = new Task<>() {
            @Override
            protected Void call() {
                Repository repository = cloneRepoToTempDir();
                if (repository != null) {
                    displayNewerCommits(repository);
                } else {
                    error("Couldn't fetch updates");
                }
                return null;
            }
        };

        fetchAndDisplayTask.setOnSucceeded(event -> showResultWindow());

        fetchAndDisplayTask.setOnFailed(event -> {
            Throwable exception = fetchAndDisplayTask.getException();
            error("Failed to fetch updates into the temporary repository: " + exception.getMessage());
            exception.printStackTrace();
        });

        return fetchAndDisplayTask;
    }

    private Repository cloneRepoToTempDir() {
        try {
            Repository localRepository = new RepositoryBuilder().setGitDir(repo).build();

            String remoteUrl = localRepository.getConfig().getString("remote", "origin", "url");

            if (remoteUrl == null || remoteUrl.isEmpty()) {
                error("Remote URL not found in the local repository.");
                return null;
            }

            tempDir = Files.createTempDirectory("tempGitRepo");
            Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(tempDir.toFile())
                    .call();

            return new RepositoryBuilder().setGitDir(tempDir.resolve(".git").toFile()).build();
        } catch (IOException | GitAPIException e) {
            error("Failed to clone repository to a temporary directory");
            return null;
        }
    }


    private void displayNewerCommits(Repository repository) {
        try (Git git = new Git(repository)) {
            Repository local = new RepositoryBuilder().setGitDir(repo).build();
            ObjectId localCommitId = local.resolve("HEAD");

            if (localCommitId != null) {
                git.fetch()
                        .setRemote("origin")
                        .call();

                Iterable<RevCommit> remoteCommits = git.log().call();

                List<String> newerCommits = new ArrayList<>();
                for (RevCommit commit : remoteCommits) {
                    if (commit.getId().equals(localCommitId)) {
                        break;
                    }
                    newerCommits.add(commit.getShortMessage());
                }

                commitListView.getItems().addAll(newerCommits);
            }
        } catch (IOException | GitAPIException e) {
            error("Couldn't display commits");
            e.printStackTrace();
        }
    }

    private void showResultWindow() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Commits");
        dialog.setHeaderText(null);

        VBox vBox = new VBox(10);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            performUpdate();
            dialog.close();
        });

        vBox.getChildren().addAll(commitListView, updateButton);

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

    private void performUpdate() {
        try (Repository repository = new RepositoryBuilder().setGitDir(repo).build();
             Git git = new Git(repository)) {

            git.clean().setCleanDirectories(true).call();
            git.reset().setMode(ResetCommand.ResetType.HARD).call();

            git.fetch().call();
            git.pull().call();

            info("Update successful!");
        } catch (IOException | GitAPIException e) {
            error("Update failed");
            e.printStackTrace();
        }
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
