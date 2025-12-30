package com.example.tournamentmanager.helper.players;

import com.example.tournamentmanager.helper.DialogHelper;
import com.example.tournamentmanager.helper.PlayerCorrectionController;
import com.example.tournamentmanager.model.Federation;
import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Title;
import com.example.tournamentmanager.model.Tournament;
import com.example.tournamentmanager.operation.FIDEOperation;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.tournamentmanager.helper.DialogHelper.error;

public class StartListHelper {
    private Tournament tournament;
    private Button correctFide;
    private Button correctPl;
    private TableView<Player> playersListTable;
    private TableColumn<Player, Integer> startNoCol;
    private TableColumn<Player, Title> titleCol;
    private TableColumn<Player, String> nameCol;
    private TableColumn<Player, Federation> fedCol;
    private TableColumn<Player, Integer> fideCol;
    private TableColumn<Player, Integer> localCol;
    private TableColumn<Player, String> clubCol;
    private TableColumn<Player, Integer> localIdCol;
    private TableColumn<Player, Integer> fideIdCol;
    private TableColumn<Player, String> remarksCol;
    private TableColumn<Player, Void> deleteCol;

    public StartListHelper(
            Tournament tournament,
            Button correctFide, Button correctPl,
            TableView<Player> playersListTable,
            TableColumn<Player, Integer> startNoCol, TableColumn<Player, Title> titleCol, TableColumn<Player, String> nameCol, TableColumn<Player, Federation> fedCol,
            TableColumn<Player, Integer> fideCol, TableColumn<Player, Integer> localCol, TableColumn<Player, String> clubCol, TableColumn<Player, Integer> localIdCol,
            TableColumn<Player, Integer> fideIdCol, TableColumn<Player, String> remarksCol, TableColumn<Player, Void> deleteCol) {
        setTournament(tournament);
        setCorrectFide(correctFide);
        setCorrectPl(correctPl);
        setPlayersListTable(playersListTable);
        setStartNoCol(startNoCol);
        setTitleCol(titleCol);
        setNameCol(nameCol);
        setFedCol(fedCol);
        setFideCol(fideCol);
        setLocalCol(localCol);
        setClubCol(clubCol);
        setLocalIdCol(localIdCol);
        setFideIdCol(fideIdCol);
        setRemarksCol(remarksCol);
        setDeleteCol(deleteCol);

        getCorrectFide().setOnAction(e -> {
            Path path;
            switch (getTournament().getType()) {
                case BLITZ -> path = Path.of("blitz_rating_list.db");
                case RAPID -> path = Path.of("rapid_rating_list.db");
                case OTHER -> {
                    error("There is no list for other type chess");
                    return;
                }
                default -> path = Path.of("standard_rating_list.db");
            }
            if (!Files.exists(path)) {
                error("Can't find fide list");
                return;
            }
            DialogHelper.ProgressMessageBox progressMessageBox = new DialogHelper.ProgressMessageBox("Progress Dialog");
            progressMessageBox.show();
            int n = getTournament().getPlayersObs().size();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    ArrayList<Player> found = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        Player player = getTournament().getPlayersObs().get(i);

                        float finalI = i + 1;
                        Platform.runLater(() -> progressMessageBox.setValue(finalI / n));

                        ArrayList<Player> players = FIDEOperation.searchSimilarFide(player, getTournament().getType());
                        if (players.size() == 1) {
                            Player playerFide = players.getFirst();
                            found.add(playerFide);
                        } else {
                            players = (ArrayList<Player>) players.stream()
                                    .filter(item -> item.getYearOfBirth() == player.getYearOfBirth())
                                    .toList();
                            if (players.size() == 1) {
                                Player playerFide = players.getFirst();
                                found.add(playerFide);
                            } else {
                                found.add(null);
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(PlayerCorrectionController.class.getResource("PlayerCorrection.fxml"));

                            PlayerCorrectionController controller = new PlayerCorrectionController(getTournament().getPlayersObs(), found, playersListTable::refresh);
                            loader.setController(controller);

                            VBox root = loader.load();
                            Stage newStage = new Stage();
                            Scene scene = new Scene(root);
                            newStage.setScene(scene);
                            newStage.setTitle("Player Correction");
                            newStage.show();
                            newStage.setMaximized(true);
                            controller.initialize();

                        } catch (IOException e) {
                            System.err.println("Error loading PlayerCorrection.fxml: " + e.getMessage());
                        }
                    });
                    return null;
                }
            };

            new Thread(task).start();
        });
        getCorrectPl().setOnAction(e -> {
            if (!Files.exists(Path.of("rejestr_czlonkow.db"))) {
                error("Can't found polish list");
                return;
            }
            DialogHelper.ProgressMessageBox progressMessageBox = new DialogHelper.ProgressMessageBox("Progress Dialog");
            progressMessageBox.show();
            int n = getTournament().getPlayersObs().size();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    ArrayList<Player> found = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        Player player = getTournament().getPlayersObs().get(i);

                        float finalI = i + 1;
                        Platform.runLater(() -> progressMessageBox.setValue(finalI / n));

                        ArrayList<Player> players = FIDEOperation.searchSimilarPol(player, getTournament().getType());
                        if (players.size() == 1) {
                            Player playerPl = players.getFirst();
                            found.add(playerPl);
                        } else {
                            players = (ArrayList<Player>) players.stream()
                                    .filter(item -> item.getYearOfBirth() == player.getYearOfBirth())
                                    .collect(Collectors.toList());
                            if (players.size() == 1) {
                                Player playerPl = players.getFirst();
                                found.add(playerPl);
                            } else {
                                players = (ArrayList<Player>) players.stream()
                                        .filter(item -> Objects.equals(item.getDateOfBirth(), player.getDateOfBirth()))
                                        .collect(Collectors.toList());
                                if (players.size() == 1) {
                                    Player playerPl = players.getFirst();
                                    found.add(playerPl);
                                } else {
                                    found.add(null);
                                }
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(PlayerCorrectionController.class.getResource("PlayerCorrection.fxml"));

                            PlayerCorrectionController controller = new PlayerCorrectionController(getTournament().getPlayersObs(), found, playersListTable::refresh);
                            loader.setController(controller);

                            VBox root = loader.load();
                            Stage newStage = new Stage();
                            Scene scene = new Scene(root);
                            newStage.setScene(scene);
                            newStage.setTitle("Player Correction");
                            newStage.show();
                            newStage.setMaximized(true);
                            controller.initialize();

                        } catch (IOException e) {
                            System.err.println("Error loading PlayerCorrection.fxml: " + e.getMessage());
                        }
                    });
                    return null;
                }
            };

            new Thread(task).start();
        });
        getPlayersListTable().setItems(tournament.getPlayersObs());

        getStartNoCol().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getPlayersListTable().getItems().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });
        getTitleCol().setCellValueFactory(new PropertyValueFactory<>("title"));
        getTitleCol().setCellFactory(param -> new ChoiceBoxTableCell<>(
                FXCollections.observableArrayList(Title.values())
        ));
        getTitleCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setTitle(event.getNewValue());
        });

        getNameCol().setCellValueFactory(new PropertyValueFactory<>("name"));
        getNameCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getNameCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setName(event.getNewValue());
        });

        getFedCol().setCellValueFactory(new PropertyValueFactory<>("federation"));
        getFedCol().setCellFactory(param -> new ChoiceBoxTableCell<>(
                FXCollections.observableArrayList(Federation.values())
        ));
        getFedCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setFederation(event.getNewValue());
        });

        getFideCol().setCellValueFactory(new PropertyValueFactory<>("fideRating"));
        getFideCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getFideCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                player.setFideRating(event.getNewValue());
            } catch (Exception ignored) {
            }
        });

        getLocalCol().setCellValueFactory(new PropertyValueFactory<>("localRating"));
        getLocalCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getLocalCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                player.setLocalRating(event.getNewValue());
            } catch (Exception ignored) {
            }
        });

        getClubCol().setCellValueFactory(new PropertyValueFactory<>("club"));
        getClubCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getClubCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setClub(event.getNewValue());
        });

        getLocalIdCol().setCellValueFactory(new PropertyValueFactory<>("localId"));
        getLocalIdCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getLocalIdCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                player.setLocalId(event.getNewValue());
            } catch (Exception ignored) {
            }
        });

        getFideIdCol().setCellValueFactory(new PropertyValueFactory<>("fideId"));
        getFideIdCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getFideIdCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                player.setFideId(event.getNewValue());
            } catch (Exception ignored) {
            }
        });

        getRemarksCol().setCellValueFactory(new PropertyValueFactory<>("remarks"));
        getRemarksCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getRemarksCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setRemarks(event.getNewValue());
        });

        getDeleteCol().setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Player player = getTableRow().getItem();
                    if (player != null) {
                        getTournament().getPlayersObs().remove(player);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(deleteButton);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Button getCorrectFide() {
        return correctFide;
    }

    public void setCorrectFide(Button correctFide) {
        this.correctFide = correctFide;
    }

    public Button getCorrectPl() {
        return correctPl;
    }

    public void setCorrectPl(Button correctPl) {
        this.correctPl = correctPl;
    }

    public TableView<Player> getPlayersListTable() {
        return playersListTable;
    }

    public void setPlayersListTable(TableView<Player> playersListTable) {
        this.playersListTable = playersListTable;
    }

    public TableColumn<Player, Integer> getStartNoCol() {
        return startNoCol;
    }

    public void setStartNoCol(TableColumn<Player, Integer> startNoCol) {
        this.startNoCol = startNoCol;
    }

    public TableColumn<Player, Title> getTitleCol() {
        return titleCol;
    }

    public void setTitleCol(TableColumn<Player, Title> titleCol) {
        this.titleCol = titleCol;
    }

    public TableColumn<Player, String> getNameCol() {
        return nameCol;
    }

    public void setNameCol(TableColumn<Player, String> nameCol) {
        this.nameCol = nameCol;
    }

    public TableColumn<Player, Federation> getFedCol() {
        return fedCol;
    }

    public void setFedCol(TableColumn<Player, Federation> fedCol) {
        this.fedCol = fedCol;
    }

    public TableColumn<Player, Integer> getFideCol() {
        return fideCol;
    }

    public void setFideCol(TableColumn<Player, Integer> fideCol) {
        this.fideCol = fideCol;
    }

    public TableColumn<Player, Integer> getLocalCol() {
        return localCol;
    }

    public void setLocalCol(TableColumn<Player, Integer> localCol) {
        this.localCol = localCol;
    }

    public TableColumn<Player, String> getClubCol() {
        return clubCol;
    }

    public void setClubCol(TableColumn<Player, String> clubCol) {
        this.clubCol = clubCol;
    }

    public TableColumn<Player, Integer> getLocalIdCol() {
        return localIdCol;
    }

    public void setLocalIdCol(TableColumn<Player, Integer> localIdCol) {
        this.localIdCol = localIdCol;
    }

    public TableColumn<Player, Integer> getFideIdCol() {
        return fideIdCol;
    }

    public void setFideIdCol(TableColumn<Player, Integer> fideIdCol) {
        this.fideIdCol = fideIdCol;
    }

    public TableColumn<Player, String> getRemarksCol() {
        return remarksCol;
    }

    public void setRemarksCol(TableColumn<Player, String> remarksCol) {
        this.remarksCol = remarksCol;
    }

    public TableColumn<Player, Void> getDeleteCol() {
        return deleteCol;
    }

    public void setDeleteCol(TableColumn<Player, Void> deleteCol) {
        this.deleteCol = deleteCol;
    }

}
