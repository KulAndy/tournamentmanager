package com.example.tournamentmanager.helper;

import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Title;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerCorrectionController {

    private final ObservableList<Player> foundPlayers;
    private final ObservableList<Player> tmpPlayers;
    private final ObservableList<Player> sourcePlayers;
    Runnable callback;
    @FXML
    private TableView<Player> playersTable;
    @FXML
    private TableColumn<Player, Integer> startNoColumn;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, Title> titleColumn;
    @FXML
    private TableColumn<Player, Integer> ratingColumn;
    @FXML
    private TableColumn<Player, Integer> fideIdColumn;
    @FXML
    private TableColumn<Player, Integer> localIdColumn;
    @FXML
    private TableColumn<Player, String> birthColumn;
    @FXML
    private TableColumn<Player, Player.Sex> sexColumn;
    @FXML
    private TableColumn<Player, String> clubColumn;
    @FXML
    private Button applyButton;
    @FXML
    private Button canelButton;
    @FXML
    private CheckBox titleHeader;
    @FXML
    private CheckBox ratingHeader;
    @FXML
    private CheckBox fideIdHeader;
    @FXML
    private CheckBox localIdHeader;
    @FXML
    private CheckBox birthHeader;
    @FXML
    private CheckBox sexHeader;
    @FXML
    private CheckBox clubHeader;

    public PlayerCorrectionController(ObservableList<Player> sourcePlayers, ArrayList<Player> foundPlayers, Runnable callback) {
        this.sourcePlayers = sourcePlayers;
        this.foundPlayers = FXCollections.observableArrayList(foundPlayers);
        this.tmpPlayers = FXCollections.observableArrayList();
        for (Player player : sourcePlayers) {
            this.tmpPlayers.add(player.clone());
        }
        this.callback = callback;
    }

    @FXML
    public void initialize() {
        titleHeader.selectedProperty().addListener(e -> {
            if (!titleHeader.isIndeterminate() && titleHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && player.getTitle() != foundPlayer.getTitle()) {
                        if (foundPlayer.getTitle() == Title.bk && foundPlayer.getLocalId() == null) {
                            tmpPlayers.get(i).setTitle(player.getTitle());
                        } else {
                            tmpPlayers.get(i).setTitle(foundPlayer.getTitle());
                        }
                    } else {
                        tmpPlayers.get(i).setTitle(player.getTitle());
                    }

                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setTitle(player.getTitle());
                }
            }
            playersTable.refresh();
        });
        ratingHeader.selectedProperty().addListener(e -> {
            if (!ratingHeader.isIndeterminate() && ratingHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getFideRating(), foundPlayer.getFideRating())) {
                        tmpPlayers.get(i).setFideRating(foundPlayer.getFideRating());
                    } else {
                        tmpPlayers.get(i).setFideRating(player.getFideRating());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setFideRating(player.getFideRating());
                }
            }
            playersTable.refresh();
        });
        fideIdHeader.selectedProperty().addListener(e -> {
            if (!fideIdHeader.isIndeterminate() && fideIdHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getFideId(), foundPlayer.getFideId())) {
                        tmpPlayers.get(i).setFideId(foundPlayer.getFideId());
                    } else {
                        tmpPlayers.get(i).setFideId(player.getFideId());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setFideId(player.getFideId());
                }
            }
            playersTable.refresh();
        });
        localIdHeader.selectedProperty().addListener(e -> {
            if (!localIdHeader.isIndeterminate() && localIdHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getLocalId(), foundPlayer.getLocalId())) {
                        tmpPlayers.get(i).setLocalId(foundPlayer.getLocalId());
                    } else {
                        tmpPlayers.get(i).setLocalId(player.getLocalId());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setLocalId(player.getLocalId());
                }
            }
            playersTable.refresh();
        });
        birthHeader.selectedProperty().addListener(e -> {
            if (!birthHeader.isIndeterminate() && birthHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && foundPlayer.getDateOfBirth() != null && !Objects.equals(player.getDateOfBirth(), foundPlayer.getDateOfBirth())
                    ) {
                        if (player.getYearOfBirth() == foundPlayer.getYearOfBirth() && foundPlayer.getMonthOfBirth() == 0 && foundPlayer.getDayOfBirth() == 0) {
                            tmpPlayers.get(i).setDateOfBirth(player.getDateOfBirth());
                        } else {
                            tmpPlayers.get(i).setDateOfBirth(foundPlayer.getDateOfBirth());
                        }
                    } else {
                        tmpPlayers.get(i).setDateOfBirth(player.getDateOfBirth());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setDateOfBirth(player.getDateOfBirth());
                }
            }
            playersTable.refresh();
        });
        sexHeader.selectedProperty().addListener(e -> {
            if (!sexHeader.isIndeterminate() && sexHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getSex(), foundPlayer.getSex())) {
                        tmpPlayers.get(i).setSex(foundPlayer.getSex());
                    } else {
                        tmpPlayers.get(i).setSex(player.getSex());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setSex(player.getSex());
                }
            }
            playersTable.refresh();
        });
        clubHeader.selectedProperty().addListener(e -> {
            if (!clubHeader.isIndeterminate() && clubHeader.isSelected()) {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    Player foundPlayer = null;
                    if (i < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(i);
                    }

                    if (foundPlayer != null && !Objects.equals(foundPlayer.getClub(), "") && !Objects.equals(player.getClub(), foundPlayer.getClub())) {
                        tmpPlayers.get(i).setClub(foundPlayer.getClub());
                    } else {
                        tmpPlayers.get(i).setClub(player.getClub());
                    }
                }
            } else {
                for (int i = 0; i < tmpPlayers.size(); i++) {
                    Player player = sourcePlayers.get(i);
                    tmpPlayers.get(i).setClub(player.getClub());
                }
            }
            playersTable.refresh();
        });

        startNoColumn.setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = playersTable.getItems().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        titleColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setTitle(foundPlayer.getTitle());
                        } else {
                            tmpPlayers.get(playerIndex).setTitle(player.getTitle());
                        }
                    }
                    if (newValue != titleHeader.isSelected() && !changeCb.isDisabled()) {
                        titleHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(Title item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && player.getTitle() != foundPlayer.getTitle()) {
                        if (foundPlayer.getTitle() == Title.bk && foundPlayer.getLocalId() == null) {
                            changeCb.setText(player.getTitle() == null ? "None" : player.getTitle().toString());
                            changeCb.setDisable(true);
                            changeCb.setStyle("-fx-text-fill: black;");
                        } else {
                            changeCb.setText((player.getTitle() == null ? "None" : player.getTitle().toString()) + "->" + (foundPlayer.getTitle() == null ? "None" : foundPlayer.getTitle().toString()));
                            changeCb.setDisable(false);
                            changeCb.setStyle("-fx-text-fill: red;");
                        }
                    } else {
                        changeCb.setText(player.getTitle() == null ? "None" : player.getTitle().toString());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    changeCb.setSelected(tmpPlayers.get(playerIndex).getTitle() != player.getTitle());
                    setGraphic(changeCb);
                }
            }
        });

        ratingColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setFideRating(foundPlayer.getFideRating());
                        } else {
                            tmpPlayers.get(playerIndex).setFideRating(player.getFideRating());
                        }
                    }
                    if (newValue != ratingHeader.isSelected() && !changeCb.isDisabled()) {
                        ratingHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getFideRating(), foundPlayer.getFideRating())) {
                        changeCb.setText(player.getFideRating().toString() + "->" + foundPlayer.getFideRating().toString());
                        changeCb.setDisable(false);
                        changeCb.setStyle("-fx-text-fill: red;");
                    } else {
                        changeCb.setText(player.getFideRating().toString());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(!Objects.equals(tmpPlayers.get(playerIndex).getFideRating(), player.getFideRating()));
                }
            }
        });

        fideIdColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setFideId(foundPlayer.getFideId());
                        } else {
                            tmpPlayers.get(playerIndex).setFideId(player.getFideId());
                        }
                    }
                    if (newValue != fideIdHeader.isSelected() && !changeCb.isDisabled()) {
                        fideIdHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getFideId(), foundPlayer.getFideId())) {
                        changeCb.setText((player.getFideId() == null ? "None" : player.getFideId().toString()) + "->" + (foundPlayer.getFideId() == null ? "None" : foundPlayer.getFideId().toString()));
                        changeCb.setDisable(false);
                        changeCb.setStyle("-fx-text-fill: red;");
                    } else {
                        changeCb.setText(player.getFideId() == null ? "None" : player.getFideId().toString());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(!Objects.equals(tmpPlayers.get(playerIndex).getFideId(), player.getFideId()));
                }
            }
        });

        localIdColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setLocalId(foundPlayer.getLocalId());
                        } else {
                            tmpPlayers.get(playerIndex).setLocalId(player.getLocalId());
                        }
                    }
                    if (newValue != localIdHeader.isSelected() && !changeCb.isDisabled()) {
                        localIdHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && foundPlayer.getLocalId() != null && !Objects.equals(player.getLocalId(), foundPlayer.getLocalId())) {
                        changeCb.setText((player.getLocalId() == null ? "None" : player.getLocalId().toString()) + "->" + (foundPlayer.getLocalId() == null ? "None" : foundPlayer.getLocalId().toString()));
                        changeCb.setDisable(false);
                        changeCb.setStyle("-fx-text-fill: red;");
                    } else {
                        changeCb.setText(player.getLocalId() == null ? "None" : player.getLocalId().toString());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(!Objects.equals(tmpPlayers.get(playerIndex).getLocalId(), player.getLocalId()));
                }
            }
        });

        birthColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setDateOfBirth(foundPlayer.getDateOfBirth());
                        } else {
                            tmpPlayers.get(playerIndex).setDateOfBirth(player.getDateOfBirth());
                        }
                    }
                    if (newValue != birthHeader.isSelected() && !changeCb.isDisabled()) {
                        birthHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && foundPlayer.getDateOfBirth() != null && !Objects.equals(player.getDateOfBirth(), foundPlayer.getDateOfBirth())
                    ) {
                        if (player.getYearOfBirth() == foundPlayer.getYearOfBirth() && foundPlayer.getMonthOfBirth() == 0 && foundPlayer.getDayOfBirth() == 0) {
                            changeCb.setText(player.getDateOfBirth());
                            changeCb.setDisable(true);
                            changeCb.setStyle("-fx-text-fill: black;");
                        } else {
                            changeCb.setText(player.getDateOfBirth() + "->" + foundPlayer.getDateOfBirth());
                            changeCb.setDisable(false);
                            changeCb.setStyle("-fx-text-fill: red;");
                        }
                    } else {
                        changeCb.setText(player.getDateOfBirth());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(!Objects.equals(tmpPlayers.get(playerIndex).getDateOfBirth(), player.getDateOfBirth()));
                }
            }
        });


        sexColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setSex(foundPlayer.getSex());
                        } else {
                            tmpPlayers.get(playerIndex).setSex(player.getSex());
                        }
                    }
                    if (newValue != sexHeader.isSelected() && !changeCb.isDisabled()) {
                        sexHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(Player.Sex item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && !Objects.equals(player.getSex(), foundPlayer.getSex())) {
                        changeCb.setText((player.getSex() == null ? "None" : player.getSex().toString()) + "->" + (foundPlayer.getSex() == null ? "None" : foundPlayer.getSex().toString()));
                        changeCb.setDisable(false);
                        changeCb.setStyle("-fx-text-fill: red;");
                    } else {
                        changeCb.setText(player.getSex() == null ? "None" : player.getSex().toString());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(tmpPlayers.get(playerIndex).getSex() != player.getSex());
                }
            }
        });

        clubColumn.setCellFactory(param -> new TableCell<>() {
            private final CheckBox changeCb = new CheckBox();


            {
                changeCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    Player player = getTableRow().getItem();
                    int playerIndex = sourcePlayers.indexOf(player);
                    Player foundPlayer = null;
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (player != null && foundPlayer != null) {
                        if (newValue) {
                            tmpPlayers.get(playerIndex).setClub(foundPlayer.getClub());
                        } else {
                            tmpPlayers.get(playerIndex).setClub(player.getClub());
                        }
                    }
                    if (newValue != clubHeader.isSelected() && !changeCb.isDisabled()) {
                        clubHeader.setIndeterminate(true);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                Player player = getTableRow().getItem();

                if (empty || player == null) {
                    setGraphic(null);
                    changeCb.setText(null);
                    changeCb.setSelected(false);
                    changeCb.setDisable(true);
                } else {
                    Player foundPlayer = null;
                    int playerIndex = sourcePlayers.indexOf(player);
                    if (playerIndex < foundPlayers.size()) {
                        foundPlayer = foundPlayers.get(playerIndex);
                    }

                    if (foundPlayer != null && !Objects.equals(foundPlayer.getClub(), "") && !Objects.equals(player.getClub(), foundPlayer.getClub())) {
                        changeCb.setText(player.getClub() + "->" + foundPlayer.getClub());
                        changeCb.setDisable(false);
                        changeCb.setStyle("-fx-text-fill: red;");
                    } else {
                        changeCb.setText(player.getClub());
                        changeCb.setDisable(true);
                        changeCb.setStyle("-fx-text-fill: black;");
                    }
                    setGraphic(changeCb);
                    changeCb.setSelected(!Objects.equals(tmpPlayers.get(playerIndex).getClub(), player.getClub()));
                }
            }
        });

        playersTable.setItems(sourcePlayers);
        titleHeader.setSelected(true);
        ratingHeader.setSelected(true);
        fideIdHeader.setSelected(true);
        localIdHeader.setSelected(true);
        birthHeader.setSelected(true);
        sexHeader.setSelected(true);
        clubHeader.setSelected(true);
    }

    @FXML
    private void onApply() {
        for (int i = 0; i < tmpPlayers.size(); i++) {
            Player player = sourcePlayers.get(i);
            Player tmp = tmpPlayers.get(i);
            if (!Objects.equals(player.toString(), tmp.toString())) {
                System.out.println(player);
                System.out.println(tmp);
                System.out.println();
            }
            player.setTitle(tmp.getTitle());
            player.setFideRating(tmp.getFideRating());
            player.setFideId(tmp.getFideId());
            player.setLocalId(tmp.getLocalId());
            player.setDateOfBirth(tmp.getDateOfBirth());
            player.setSex(tmp.getSex());
            player.setClub(tmp.getClub());

        }
        callback.run();
        onClose();
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }
}
