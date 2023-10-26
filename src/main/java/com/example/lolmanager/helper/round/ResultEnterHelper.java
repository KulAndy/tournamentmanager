package com.example.lolmanager.helper.round;

import com.example.lolmanager.comparator.PairingComparator;
import com.example.lolmanager.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.lolmanager.helper.GeneralHelper.*;

public class ResultEnterHelper {
    private final IntegerProperty currentRoundNo = new SimpleIntegerProperty();
    private Tournament tournament;
    private ComboBox<Integer> roundsViewSelect;
    private Button firstRound;
    private Button previousRound;
    private Button nextRound;
    private Button lastRound;
    private Button whiteWinResult;
    private Button drawResult;
    private Button blackWinResult;
    private Button whiteWinForfeitResult;
    private Button blackWinForfeitResult;
    private Button applyResultButton;
    private TableView<Game> gamesView;
    private TableColumn<Game, Integer> leftBoardNo;
    private TableColumn<Game, Float> whitePoints;
    private TableColumn<Game, Integer> whiteRating;
    private TableColumn<Game, String> whitePlayer;
    private TableColumn<Game, Void> gameResult;
    private TableColumn<Game, String> blackPlayer;
    private TableColumn<Game, Integer> blackRating;
    private TableColumn<Game, Float> blackPoints;
    private TableColumn<Game, Integer> rightBoardNo;
    private Button enginePairButton;
    private ObservableList<Integer> roundsNumbersObs = FXCollections.observableArrayList();
    private ObservableList<Game> currentRound = FXCollections.observableArrayList();
    private Button deleteRound;
    private final Engine engine = new JavafoWrapper();

    public ResultEnterHelper(
            Tournament tournament,
            ComboBox<Integer> roundsViewSelect, Button firstRound, Button previousRound, Button nextRound, Button lastRound,
            Button whiteWinResult, Button drawResult, Button blackWinResult, Button whiteWinForfeitResult, Button blackWinForfeitResult,
            Button applyResultButton, TableView<Game> gamesView, TableColumn<Game, Integer> leftBoardNo, TableColumn<Game, Float> whitePoints,
            TableColumn<Game, Integer> whiteRating, TableColumn<Game, String> whitePlayer, TableColumn<Game, Void> gameResult,
            TableColumn<Game, String> blackPlayer, TableColumn<Game, Integer> blackRating, TableColumn<Game, Float> blackPoints,
            TableColumn<Game, Integer> rightBoardNo, Button deleteRound, Button enginePairButton
    ) {
        setTournament(tournament);
        setRoundsViewSelect(roundsViewSelect);
        getRoundsViewSelect().setItems(roundsNumbersObs);
        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            ArrayList<Integer> rounds = new ArrayList<>();
            for (int i = 1; i <= getTournament().getRoundsObs().size(); i++) {
                rounds.add(i);
            }
            roundsNumbersObs = FXCollections.observableArrayList(rounds);
            getRoundsViewSelect().setItems(roundsNumbersObs);
            if (getRoundsViewSelect().getValue() == null && !rounds.isEmpty()) {
                getRoundsViewSelect().setValue(rounds.get(0));
            }
        });


        getRoundsViewSelect().valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            if (newValue != null) {
                try {
                    setCurrentRound(FXCollections.observableArrayList(getTournament().getRoundsObs().get(newValue - 1)));
                } catch (Exception e) {
                    setCurrentRound(FXCollections.observableArrayList());
                }
                getGamesView().setItems(getCurrentRound());
                currentRoundNo.set(newValue);
                getGamesView().refresh();
            }
        });

        setFirstRound(firstRound);
        setPreviousRound(previousRound);
        setNextRound(nextRound);
        setLastRound(lastRound);
        setWhiteWinResult(whiteWinResult);
        setDrawResult(drawResult);
        setBlackWinResult(blackWinResult);
        setWhiteWinForfeitResult(whiteWinForfeitResult);
        setBlackWinForfeitResult(blackWinForfeitResult);
        setDeleteRound(deleteRound);
        setApplyResultButton(applyResultButton);
        getApplyResultButton().setOnAction(e -> {
            boolean forfeitIncompatible = false;
            boolean pointsOverflow = false;
            for (int i = 0; i < getCurrentRound().size(); i++) {
                TextField textField1 = (TextField) getGamesView().lookup("#result" + i + "white");
                TextField textField2 = (TextField) getGamesView().lookup("#result" + i + "black");
                if (textField1 != null && textField2 != null) {
                    Object[] objects1 = Result.getResultFromPoints(textField1.getText().trim());
                    Result whiteResult = (Result) objects1[0];
                    boolean forfeit1 = (boolean) objects1[1];
                    Object[] objects2 = Result.getResultFromPoints(textField2.getText());
                    Result blackResult = (Result) objects2[0];
                    boolean forfeit2 = (boolean) objects2[1];
                    if (forfeit1 != forfeit2) {
                        forfeitIncompatible = true;
                    }

                    if (
                            whiteResult == Result.WIN && (blackResult == Result.WIN || blackResult == Result.DRAW)
                                    || whiteResult == Result.DRAW && blackResult == Result.WIN
                    ) {
                        pointsOverflow = true;
                    }
                }
            }

            if (forfeitIncompatible && pointsOverflow) {
                error("Incompatible results - occured in one game result for played and unplayed game\nPoints overflow - in one game both players total has more than points for winning");
            } else if (forfeitIncompatible) {
                error("Incompatible results - occured in one game result for played and unplayed game");
            } else if (pointsOverflow) {
                error("Points overflow - in one game both players total has more than points for winning");
            } else {
                for (int i = 0; i < getCurrentRound().size(); i++) {
                    TextField textField1 = (TextField) getGamesView().lookup("#result" + i + "white");
                    TextField textField2 = (TextField) getGamesView().lookup("#result" + i + "black");
                    if (textField1 != null && textField2 != null) {
                        Object[] objects1 = Result.getResultFromPoints(textField1.getText().trim());
                        Result whiteResult = (Result) objects1[0];
                        boolean forfeit1 = (boolean) objects1[1];
                        Object[] objects2 = Result.getResultFromPoints(textField2.getText().trim());
                        Result blackResult = (Result) objects2[0];
                        boolean forfeit2 = (boolean) objects2[1];
                        Game game = getTournament().getRoundsObs().get(currentRoundNo.get() - 1).get(i);
                        game.setWhiteResult(whiteResult);
                        game.setBlackResult(blackResult);
                        game.setForfeit(forfeit1 && forfeit2);
                    }
                }
                int lastRoundIndex = getTournament().getRoundsObs().size() - 1;
                ArrayList<Game> lastRoundElem = new ArrayList<>(getTournament().getRoundsObs().get(lastRoundIndex));
                getTournament().getRoundsObs().remove(lastRoundIndex);
                getTournament().getRoundsObs().add(lastRoundElem);
            }
        });

        setGamesView(gamesView);
        getGamesView().setItems(getCurrentRound());

        setLeftBoardNo(leftBoardNo);
        getLeftBoardNo().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            int rowIndex = getGamesView().getItems().indexOf(game) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });

        setWhitePoints(whitePoints);
        getWhitePoints().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player white = game.getWhite();
            return new SimpleFloatProperty(white.getPointInRound(currentRoundNo.get() - 1)).asObject();
        });
        setWhiteRating(whiteRating);
        getWhiteRating().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player white = game.getWhite();
            return new SimpleIntegerProperty(white.getFideRating()).asObject();
        });
        setWhitePlayer(whitePlayer);
        getWhitePlayer().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player white = game.getWhite();
            return new SimpleStringProperty(white.getName());
        });
        setGameResult(gameResult);
        getGameResult().setCellFactory(column -> new TableCell<>() {
            private final HBox hbox = new HBox();
            private final TextField textField1 = new TextField();
            private final Label separator = new Label("-");
            private final TextField textField2 = new TextField();

            {
                hbox.getChildren().addAll(textField1, separator, textField2);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Game game = getTableRow().getItem();
                    Player bye = getTournament().getPlayers().getBye();
                    Player halfbye = getTournament().getPlayers().getHalfbye();
                    Player unpaired = getTournament().getPlayers().getUnpaired();
                    Player black = game.getBlack();

                    if (black.equals(bye)) {
                        textField1.setText(String.valueOf(Player.getPointsForBye()));
                        textField2.setText(String.valueOf(1.0 - (Player.getPointsForBye())));
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else if (black.equals(halfbye)) {
                        textField1.setText(String.valueOf(Player.getPointsForHalfBye()));
                        textField2.setText(String.valueOf(1.0 - (Player.getPointsForHalfBye())));
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else if (black.equals(unpaired)) {
                        textField1.setText("-");
                        textField2.setText("+");
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else {
                        textField1.setText(Result.getResultString(game.getWhiteResult(), game.isForfeit()));
                        textField2.setText(Result.getResultString(game.getBlackResult(), game.isForfeit()));
                        textField1.setId("result" + getIndex() + "white");
                        textField2.setId("result" + getIndex() + "black");
                        textField1.setDisable(false);
                        textField2.setDisable(false);
                    }
                    setGraphic(hbox);
                }
            }
        });

        setBlackPlayer(blackPlayer);
        getBlackPlayer().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player black = game.getBlack();
            return new SimpleStringProperty(black.getName());
        });
        setBlackRating(blackRating);
        getBlackRating().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player black = game.getBlack();
            return new SimpleIntegerProperty(black.getFideRating()).asObject();
        });
        setBlackPoints(blackPoints);
        getBlackPoints().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            Player black = game.getBlack();
            return new SimpleFloatProperty(black.getPointInRound(currentRoundNo.get() - 1)).asObject();
        });
        setRightBoardNo(rightBoardNo);
        getRightBoardNo().setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            int rowIndex = getGamesView().getItems().indexOf(game) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });


        firstRound.setOnAction(e -> {
            if (!roundsNumbersObs.isEmpty()) {
                getRoundsViewSelect().getSelectionModel().selectFirst();
            }
        });

        nextRound.setOnAction(e -> {
            if (!roundsNumbersObs.isEmpty()) {
                getRoundsViewSelect().getSelectionModel().selectNext();
            }
        });

        previousRound.setOnAction(e -> {
            if (!roundsNumbersObs.isEmpty()) {
                getRoundsViewSelect().getSelectionModel().selectPrevious();
            }
        });

        lastRound.setOnAction(e -> {
            if (!roundsNumbersObs.isEmpty()) {
                getRoundsViewSelect().getSelectionModel().selectLast();
            }
        });

        deleteRound.setOnAction(e -> {
            int index = getRoundsViewSelect().getSelectionModel().getSelectedIndex();
            int last = getRoundsViewSelect().getItems().size() - 1;
            if (index == last) {
                if (roundsNumbersObs.size() > 1) {
                    getRoundsViewSelect().getSelectionModel().selectPrevious();
                } else {
                    getRoundsViewSelect().setValue(null);
                }
                getTournament().getRoundsObs().remove(index);
            } else {
                confirm("This will also remove subsequent rounds")
                        .thenAccept(result -> {
                            if (result) {
                                if (roundsNumbersObs.size() > 1) {
                                    getRoundsViewSelect().getSelectionModel().selectPrevious();
                                } else {
                                    getRoundsViewSelect().setValue(null);
                                }
                                getTournament().getRoundsObs().remove(index, last + 1);
                            }
                        });
            }
        });

        setEnginePairButton(enginePairButton);
        getEnginePairButton().setOnAction(e -> {
            if (getTournament().getRounds().size() < getTournament().getRoundsNumber()) {

                try {
                    ArrayList<Game> pairing = engine.generatePairing(getTournament());
                    getRoundsViewSelect().getSelectionModel().selectLast();
                    info("Paired successfully\nGenerated " + pairing.size() + " pairings");
                } catch (IOException | InterruptedException ex) {
                    error("An error occurred during pairing");
                }
            } else {
                error("Unable to pairing - number of rounds in tournament reached");
            }
        });
    }

    public ComboBox<Integer> getRoundsViewSelect() {
        return roundsViewSelect;
    }

    public void setRoundsViewSelect(ComboBox<Integer> roundsViewSelect) {
        this.roundsViewSelect = roundsViewSelect;
    }

    public Button getFirstRound() {
        return firstRound;
    }

    public void setFirstRound(Button firstRound) {
        this.firstRound = firstRound;
    }

    public Button getPreviousRound() {
        return previousRound;
    }

    public void setPreviousRound(Button previousRound) {
        this.previousRound = previousRound;
    }

    public Button getNextRound() {
        return nextRound;
    }

    public void setNextRound(Button nextRound) {
        this.nextRound = nextRound;
    }

    public Button getLastRound() {
        return lastRound;
    }

    public void setLastRound(Button lastRound) {
        this.lastRound = lastRound;
    }

    public Button getWhiteWinResult() {
        return whiteWinResult;
    }

    public void setWhiteWinResult(Button whiteWinResult) {
        this.whiteWinResult = whiteWinResult;
    }

    public Button getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(Button drawResult) {
        this.drawResult = drawResult;
    }

    public Button getBlackWinResult() {
        return blackWinResult;
    }

    public void setBlackWinResult(Button blackWinResult) {
        this.blackWinResult = blackWinResult;
    }

    public Button getWhiteWinForfeitResult() {
        return whiteWinForfeitResult;
    }

    public void setWhiteWinForfeitResult(Button whiteWinForfeitResult) {
        this.whiteWinForfeitResult = whiteWinForfeitResult;
    }

    public Button getBlackWinForfeitResult() {
        return blackWinForfeitResult;
    }

    public void setBlackWinForfeitResult(Button blackWinForfeitResult) {
        this.blackWinForfeitResult = blackWinForfeitResult;
    }

    public Button getApplyResultButton() {
        return applyResultButton;
    }

    public void setApplyResultButton(Button applyResultButton) {
        this.applyResultButton = applyResultButton;
    }

    public TableView<Game> getGamesView() {
        return gamesView;
    }

    public void setGamesView(TableView<Game> gamesView) {
        this.gamesView = gamesView;
    }

    public TableColumn<Game, Integer> getLeftBoardNo() {
        return leftBoardNo;
    }

    public void setLeftBoardNo(TableColumn<Game, Integer> leftBoardNo) {
        this.leftBoardNo = leftBoardNo;
    }

    public TableColumn<Game, Float> getWhitePoints() {
        return whitePoints;
    }

    public void setWhitePoints(TableColumn<Game, Float> whitePoints) {
        this.whitePoints = whitePoints;
    }

    public TableColumn<Game, Integer> getWhiteRating() {
        return whiteRating;
    }

    public void setWhiteRating(TableColumn<Game, Integer> whiteRating) {
        this.whiteRating = whiteRating;
    }

    public TableColumn<Game, String> getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(TableColumn<Game, String> whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public TableColumn<Game, Void> getGameResult() {
        return gameResult;
    }

    public void setGameResult(TableColumn<Game, Void> gameResult) {
        this.gameResult = gameResult;
    }

    public TableColumn<Game, String> getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(TableColumn<Game, String> blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public TableColumn<Game, Integer> getBlackRating() {
        return blackRating;
    }

    public void setBlackRating(TableColumn<Game, Integer> blackRating) {
        this.blackRating = blackRating;
    }

    public TableColumn<Game, Float> getBlackPoints() {
        return blackPoints;
    }

    public void setBlackPoints(TableColumn<Game, Float> blackPoints) {
        this.blackPoints = blackPoints;
    }

    public TableColumn<Game, Integer> getRightBoardNo() {
        return rightBoardNo;
    }

    public void setRightBoardNo(TableColumn<Game, Integer> rightBoardNo) {
        this.rightBoardNo = rightBoardNo;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Button getEnginePairButton() {
        return enginePairButton;
    }

    public void setEnginePairButton(Button enginePairButton) {
        this.enginePairButton = enginePairButton;
    }

    public ObservableList<Integer> getRoundsNumbersObs() {
        return roundsNumbersObs;
    }

    public void setRoundsNumbersObs(ObservableList<Integer> roundsNumbersObs) {
        this.roundsNumbersObs = roundsNumbersObs;
    }

    public ObservableList<Game> getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(ObservableList<Game> currentRound) {
        this.currentRound = currentRound;
        this.currentRound.sort(new PairingComparator(getTournament().getPlayersObs()));
    }

    public Button getDeleteRound() {
        return deleteRound;
    }

    public void setDeleteRound(Button deleteRound) {
        this.deleteRound = deleteRound;
    }

}
