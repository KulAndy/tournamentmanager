package com.example.lolmanager.helper.players;

import com.example.lolmanager.model.Federation;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class StartListHelper {
    private Tournament tournament;
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
            Tournament tournament, TableView<Player> playersListTable,
            TableColumn<Player, Integer> startNoCol, TableColumn<Player, Title> titleCol, TableColumn<Player, String> nameCol, TableColumn<Player, Federation> fedCol,
            TableColumn<Player, Integer> fideCol, TableColumn<Player, Integer> localCol, TableColumn<Player, String> clubCol, TableColumn<Player, Integer> localIdCol,
            TableColumn<Player, Integer> fideIdCol, TableColumn<Player, String> remarksCol, TableColumn<Player, Void> deleteCol) {
        setTournament(tournament);
        setPlayersListTable(playersListTable);
        getPlayersListTable().setItems(tournament.getPlayersObs());

        setStartNoCol(startNoCol);
        getStartNoCol().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getPlayersListTable().getItems().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });
        setTitleCol(titleCol);
        getTitleCol().setCellValueFactory(new PropertyValueFactory<Player, Title>("title"));
        getTitleCol().setCellFactory(param -> new ChoiceBoxTableCell<>(
                FXCollections.observableArrayList(Title.values())
        ));
        getTitleCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setTitle(event.getNewValue());
            Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
            playerStatic.setTitle(event.getNewValue());

        });

        setNameCol(nameCol);
        getNameCol().setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        getNameCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getNameCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setName(event.getNewValue());
            Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
            playerStatic.setName(event.getNewValue());
        });

        setFedCol(fedCol);
        getFedCol().setCellValueFactory(new PropertyValueFactory<Player, Federation>("federation"));
        getFedCol().setCellFactory(param -> new ChoiceBoxTableCell<>(
                FXCollections.observableArrayList(Federation.values())
        ));
        getFedCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setFederation(event.getNewValue());
            Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
            playerStatic.setFederation(event.getNewValue());
        });

        setFideCol(fideCol);
        getFideCol().setCellValueFactory(new PropertyValueFactory<Player, Integer>("fideRating"));
        getFideCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getFideCol().setOnEditCommit(event -> {
            try {
                Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
                player.setFideRating( event.getNewValue());
                Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
                playerStatic.setFideRating(event.getNewValue());
            } catch (Exception ignored) {
            }
        });

        setLocalCol(localCol);
        getLocalCol().setCellValueFactory(new PropertyValueFactory<Player, Integer>("localRating"));
        getLocalCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getLocalCol().setOnEditCommit(event -> {
            try {
                Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
                player.setLocalRating(event.getNewValue());
                Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
                playerStatic.setLocalRating(event.getNewValue());
            } catch (Exception e) {
            }
        });

        setClubCol(clubCol);
        getClubCol().setCellValueFactory(new PropertyValueFactory<Player, String>("club"));
        getClubCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getClubCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setClub(event.getNewValue());
            Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
            playerStatic.setClub(event.getNewValue());
        });

        setLocalIdCol(localIdCol);
        getLocalIdCol().setCellValueFactory(new PropertyValueFactory<Player, Integer>("localId"));
        getLocalIdCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getLocalIdCol().setOnEditCommit(event -> {
            try {
                Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
                player.setLocalId(event.getNewValue());
                Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
                playerStatic.setLocalId(event.getNewValue());
            } catch (Exception e) {
            }
        });

        setFideIdCol(fideIdCol);
        getFideIdCol().setCellValueFactory(new PropertyValueFactory<Player, Integer>("fideId"));
        getFideIdCol().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        getFideIdCol().setOnEditCommit(event -> {
            try {
                Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
                player.setFideId(event.getNewValue());
                Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
                playerStatic.setFideId(event.getNewValue());
            } catch (Exception e) {
            }
        });

        setRemarksCol(remarksCol);
        getRemarksCol().setCellValueFactory(new PropertyValueFactory<Player, String>("remarks"));
        getRemarksCol().setCellFactory(TextFieldTableCell.forTableColumn());
        getRemarksCol().setOnEditCommit(event -> {
            Player player = event.getTableView().getItems().get(event.getTablePosition().getRow());
            player.setRemarks(event.getNewValue());
            Player playerStatic = getTournament().getPlayers().get(player.getPlayerid());
            playerStatic.setRemarks(event.getNewValue());
        });

        setDeleteCol(deleteCol);
        getDeleteCol().setCellFactory(param -> new TableCell<Player, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Player player = getTableRow().getItem();
                    if (player != null) {
                        tournament.getPlayersObs().remove(player);
                        getTournament().getPlayers().remove(player);
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

    public void setDeleteCol(TableColumn deleteCol) {
        this.deleteCol = deleteCol;
    }

}
