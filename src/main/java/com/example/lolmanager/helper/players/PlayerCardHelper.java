package com.example.lolmanager.helper.players;

import com.example.lolmanager.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class PlayerCardHelper {
    private Tournament tournament;
    private ComboBox<Player> playerCardSelect;
    private Label playerCardName;
    private Label playerCardTB1;
    private Label playerCardTB1Value;
    private Label playerCardTB2;
    private Label playerCardTB2Value;
    private Label playerCardTB3;
    private Label playerCardTB3Value;
    private Label playerCardTB4;
    private Label playerCardTB4Value;
    private Label playerCardTB5;
    private Label playerCardTB5Value;
    private Label playerCardElo;
    private Label playerCardEloValue;
    private Label playerCardPZSzach;
    private Label playerCardPZSzachValue;
    private TableView<Game> playerCardGames;
    private TableColumn<Game,Integer> playerCardOppRound;
    private TableColumn<Game, Player.Color> playerCardOppColor;
    private TableColumn<Game, String> playerCardOppResult;
    private TableColumn<Game, Title> playerCardOppTitle;
    private TableColumn<Game, String> playerCardOppName;
    private TableColumn<Game, Integer> playerCardOppRtg;


    public PlayerCardHelper(
            Tournament tournament,
            ComboBox<Player> playerCardSelect,
            Label playerCardName, Label playerCardTB1, Label playerCardTB1Value,
            Label playerCardTB2, Label playerCardTB2Value, Label playerCardTB3, Label playerCardTB3Value,
            Label playerCardTB4, Label playerCardTB4Value, Label playerCardTB5, Label playerCardTB5Value,
            Label playerCardElo, Label playerCardEloValue, Label playerCardPZSzach, Label playerCardPZSzachValue,
            TableView<Game> playerCardGames, TableColumn<Game,Integer> playerCardOppRound,
            TableColumn<Game, Player.Color> playerCardOppColor, TableColumn<Game, String> playerCardOppResult,
            TableColumn<Game, Title> playerCardOppTitle, TableColumn<Game, String> playerCardOppName,
            TableColumn<Game, Integer> playerCardOppRtg, GridPane cardGrid
    ) {
        setTournament(tournament);
        setPlayerCardSelect(playerCardSelect);
        getPlayerCardSelect().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        getPlayerCardSelect().setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        getPlayerCardSelect().setItems(getTournament().getPlayersObs());
        setPlayerCardName(playerCardName);
        setPlayerCardTB1(playerCardTB1);
        getPlayerCardTB1().setText(String.valueOf(getTournament().getTiebreak().getTiebreak1()));
        setPlayerCardTB1Value(playerCardTB1Value);
        setPlayerCardTB2(playerCardTB2);
        getPlayerCardTB2().setText(String.valueOf(getTournament().getTiebreak().getTiebreak2()));
        setPlayerCardTB2Value(playerCardTB2Value);
        setPlayerCardTB3(playerCardTB3);
        getPlayerCardTB3().setText(String.valueOf(getTournament().getTiebreak().getTiebreak3()));
        setPlayerCardTB3Value(playerCardTB3Value);
        setPlayerCardTB4(playerCardTB4);
        getPlayerCardTB4().setText(String.valueOf(getTournament().getTiebreak().getTiebreak4()));
        setPlayerCardTB4Value(playerCardTB4Value);
        setPlayerCardTB5(playerCardTB5);
        getPlayerCardTB5().setText(String.valueOf(getTournament().getTiebreak().getTiebreak5()));
        setPlayerCardTB5Value(playerCardTB5Value);
        setPlayerCardElo(playerCardElo);
        setPlayerCardEloValue(playerCardEloValue);
        setPlayerCardPZSzach(playerCardPZSzach);
        setPlayerCardPZSzachValue(playerCardPZSzachValue);

        setPlayerCardGames(playerCardGames);
        setPlayerCardOppRound(playerCardOppRound);
        setPlayerCardOppColor(playerCardOppColor);
        setPlayerCardOppResult(playerCardOppResult);
        setPlayerCardOppTitle(playerCardOppTitle);
        setPlayerCardOppName(playerCardOppName);
        setPlayerCardOppRtg(playerCardOppRtg);

        getPlayerCardSelect().valueProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) ->{
            if (newValue != null) {
                getPlayerCardName().setText( "\t" + newValue.getTitle() + " " + newValue.getName());
                getPlayerCardTB1Value().setText(newValue.getTiebreak(getTournament().getTiebreak().getTiebreak1()).toString());
                getPlayerCardTB2Value().setText(newValue.getTiebreak(getTournament().getTiebreak().getTiebreak2()).toString());
                getPlayerCardTB3Value().setText(newValue.getTiebreak(getTournament().getTiebreak().getTiebreak3()).toString());
                getPlayerCardTB4Value().setText(newValue.getTiebreak(getTournament().getTiebreak().getTiebreak4()).toString());
                getPlayerCardTB5Value().setText(newValue.getTiebreak(getTournament().getTiebreak().getTiebreak5()).toString());
                getPlayerCardEloValue().setText(String.valueOf(newValue.getRatingPerformanceFide()));
                getPlayerCardPZSzachValue().setText(String.valueOf(newValue.getRatingPerformancePZSzach()));

                getPlayerCardGames().setItems(FXCollections.observableArrayList(newValue.getRounds()));
            }
        });

        getPlayerCardOppRound().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            int rowIndex = player.getRounds().indexOf(game) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });

        getPlayerCardOppColor().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            return new SimpleObjectProperty<>(player.getRoundColor(game));
        });

        getPlayerCardOppResult().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            Player.Color color = player.getRoundColor(game);
            String result = "-";
            if (game.isForfeit()){
                if (
                        (color == Player.Color.WHITE && game.getWhiteResult() == Result.WIN)
                                || (color == Player.Color.BLACK && game.getBlackResult() == Result.WIN)
                ){
                    result = "+";
                }
            }else{
                if (
                        (color == Player.Color.WHITE && game.getWhiteResult() == Result.WIN)
                                || (color == Player.Color.BLACK && game.getBlackResult() == Result.WIN)
                ){
                    result = "1";
                } else if (
                        (color == Player.Color.WHITE && game.getWhiteResult() == Result.DRAW)
                                || (color == Player.Color.BLACK && game.getBlackResult() == Result.DRAW)
                ) {
                    result = "\u00BD";
                }else {
                    result = "0";
                }
            }

            return new SimpleStringProperty(result);
        });

        getPlayerCardOppTitle().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            Player opponent = player.getOpponent(game);
            return new SimpleObjectProperty<>(opponent.getTitle());
        });

        getPlayerCardOppName().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            Player opponent = player.getOpponent(game);
            return new SimpleObjectProperty<>(opponent.getName());
        });

        getPlayerCardOppRtg().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player player = getPlayerCardSelect().getValue();
            Player opponent = player.getOpponent(game);
            return new SimpleIntegerProperty(opponent.getFideRating()).asObject();
        });

    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public ComboBox<Player> getPlayerCardSelect() {
        return playerCardSelect;
    }

    public void setPlayerCardSelect(ComboBox<Player> playerCardSelect) {
        this.playerCardSelect = playerCardSelect;
    }

    public Label getPlayerCardName() {
        return playerCardName;
    }

    public void setPlayerCardName(Label playerCardName) {
        this.playerCardName = playerCardName;
    }

    public Label getPlayerCardTB1() {
        return playerCardTB1;
    }

    public void setPlayerCardTB1(Label playerCardTB1) {
        this.playerCardTB1 = playerCardTB1;
    }

    public Label getPlayerCardTB1Value() {
        return playerCardTB1Value;
    }

    public void setPlayerCardTB1Value(Label playerCardTB1Value) {
        this.playerCardTB1Value = playerCardTB1Value;
    }

    public Label getPlayerCardTB2() {
        return playerCardTB2;
    }

    public void setPlayerCardTB2(Label playerCardTB2) {
        this.playerCardTB2 = playerCardTB2;
    }

    public Label getPlayerCardTB2Value() {
        return playerCardTB2Value;
    }

    public void setPlayerCardTB2Value(Label playerCardTB2Value) {
        this.playerCardTB2Value = playerCardTB2Value;
    }

    public Label getPlayerCardTB3() {
        return playerCardTB3;
    }

    public void setPlayerCardTB3(Label playerCardTB3) {
        this.playerCardTB3 = playerCardTB3;
    }

    public Label getPlayerCardTB3Value() {
        return playerCardTB3Value;
    }

    public void setPlayerCardTB3Value(Label playerCardTB3Value) {
        this.playerCardTB3Value = playerCardTB3Value;
    }

    public Label getPlayerCardTB4() {
        return playerCardTB4;
    }

    public void setPlayerCardTB4(Label playerCardTB4) {
        this.playerCardTB4 = playerCardTB4;
    }

    public Label getPlayerCardTB4Value() {
        return playerCardTB4Value;
    }

    public void setPlayerCardTB4Value(Label playerCardTB4Value) {
        this.playerCardTB4Value = playerCardTB4Value;
    }

    public Label getPlayerCardTB5() {
        return playerCardTB5;
    }

    public void setPlayerCardTB5(Label playerCardTB5) {
        this.playerCardTB5 = playerCardTB5;
    }

    public Label getPlayerCardTB5Value() {
        return playerCardTB5Value;
    }

    public void setPlayerCardTB5Value(Label playerCardTB5Value) {
        this.playerCardTB5Value = playerCardTB5Value;
    }

    public Label getPlayerCardElo() {
        return playerCardElo;
    }

    public void setPlayerCardElo(Label playerCardElo) {
        this.playerCardElo = playerCardElo;
    }

    public Label getPlayerCardEloValue() {
        return playerCardEloValue;
    }

    public void setPlayerCardEloValue(Label playerCardEloValue) {
        this.playerCardEloValue = playerCardEloValue;
    }

    public Label getPlayerCardPZSzach() {
        return playerCardPZSzach;
    }

    public void setPlayerCardPZSzach(Label playerCardPZSzach) {
        this.playerCardPZSzach = playerCardPZSzach;
    }

    public Label getPlayerCardPZSzachValue() {
        return playerCardPZSzachValue;
    }

    public void setPlayerCardPZSzachValue(Label playerCardPZSzachValue) {
        this.playerCardPZSzachValue = playerCardPZSzachValue;
    }

    public TableView<Game> getPlayerCardGames() {
        return playerCardGames;
    }

    public void setPlayerCardGames(TableView<Game> playerCardGames) {
        this.playerCardGames = playerCardGames;
    }

    public TableColumn<Game, Integer> getPlayerCardOppRound() {
        return playerCardOppRound;
    }

    public void setPlayerCardOppRound(TableColumn<Game, Integer> playerCardOppRound) {
        this.playerCardOppRound = playerCardOppRound;
    }

    public TableColumn<Game, Player.Color> getPlayerCardOppColor() {
        return playerCardOppColor;
    }

    public void setPlayerCardOppColor(TableColumn<Game, Player.Color> playerCardOppColor) {
        this.playerCardOppColor = playerCardOppColor;
    }

    public TableColumn<Game, String> getPlayerCardOppResult() {
        return playerCardOppResult;
    }

    public void setPlayerCardOppResult(TableColumn<Game, String> playerCardOppResult) {
        this.playerCardOppResult = playerCardOppResult;
    }

    public TableColumn<Game, Title> getPlayerCardOppTitle() {
        return playerCardOppTitle;
    }

    public void setPlayerCardOppTitle(TableColumn<Game, Title> playerCardOppTitle) {
        this.playerCardOppTitle = playerCardOppTitle;
    }

    public TableColumn<Game, String> getPlayerCardOppName() {
        return playerCardOppName;
    }

    public void setPlayerCardOppName(TableColumn<Game, String> playerCardOppName) {
        this.playerCardOppName = playerCardOppName;
    }

    public TableColumn<Game, Integer> getPlayerCardOppRtg() {
        return playerCardOppRtg;
    }

    public void setPlayerCardOppRtg(TableColumn<Game, Integer> playerCardOppRtg) {
        this.playerCardOppRtg = playerCardOppRtg;
    }
}
