package com.example.tournamentmanager.helper;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.tournamentmanager.helper.GeneralHelper.error;
import static com.example.tournamentmanager.helper.GeneralHelper.info;

public class CommitViewer {

    private ListView<String> commitListView;
    private final File repo = new File(".", ".git");

    public void displayCommitViewer() {
        if (!repo.exists()) {
            error("Couldn't check version history");
            return;
        }
        commitListView = new ListView<>();

        try (Repository repository = new RepositoryBuilder().setGitDir(repo).build()) {
            try (Git git = new Git(repository)) {
                PullResult pullResult = git.pull().call();
                if (!pullResult.isSuccessful()) {
                    error("Failed to fetch updates from the remote repository.");
                    return;
                }

                ObjectId currentCommitId = repository.resolve("HEAD");

                if (currentCommitId != null) {
                    List<String> newerCommits = new ArrayList<>();

                    Iterable<RevCommit> commits = git.log().call();
                    for (RevCommit commit : commits) {
                        if (commit.getId().equals(currentCommitId)) {
                            break;
                        }
                        newerCommits.add(commit.getShortMessage());
                    }

                    commitListView.getItems().addAll(newerCommits);

                    showResultWindow();
                }
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void showResultWindow() {
        Stage resultStage = new Stage();
        resultStage.setTitle("Commits");

        VBox vBox = new VBox(10);

        Button mergeButton = new Button("Update to selected version");
        mergeButton.setOnAction(e -> mergeSelectedCommits());

        vBox.getChildren().addAll(commitListView, mergeButton);

        Scene scene = new Scene(vBox, 600, 400);
        resultStage.setScene(scene);
        resultStage.show();
    }

    private void mergeSelectedCommits() {
        String selectedCommit = commitListView.getSelectionModel().getSelectedItem();

        if (selectedCommit != null) {
            try (Repository repository = new RepositoryBuilder().setGitDir(repo).build()) {
                try (Git git = new Git(repository)) {
                    git.clean().setCleanDirectories(true).call();
                    git.reset().setMode(ResetCommand.ResetType.HARD).setRef(selectedCommit).call();

                    MergeResult mergeResult = git.merge().include(repository.resolve(selectedCommit)).call();

                    if (mergeResult.getMergeStatus().isSuccessful()) {
                        info("Update successful!");
                    } else {
                        error("Update failed: " + mergeResult.getMergeStatus().toString());
                    }
                }
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }
        }
    }
}
