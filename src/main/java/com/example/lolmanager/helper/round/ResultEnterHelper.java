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
    private int pairEnterCounter = 0;

    public ResultEnterHelper(
            Tournament tournament,
            ComboBox<Integer> roundsViewSelect, Button firstRound, Button previousRound, Button nextRound, Button lastRound,
            Button whiteWinResult, Button drawResult, Button blackWinResult, Button whiteWinForfeitResult, Button blackWinForfeitResult,
            TableView<Game> gamesView, TableColumn<Game, Integer> leftBoardNo, TableColumn<Game, Float> whitePoints,
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
        getWhiteWinResult().setOnAction(e -> enterResult("1", "0"));
        setDrawResult(drawResult);
        getDrawResult().setOnAction(e -> enterResult("0.5", "0.5"));
        setBlackWinResult(blackWinResult);
        getBlackWinResult().setOnAction(e -> enterResult("0", "1"));
        setWhiteWinForfeitResult(whiteWinForfeitResult);
        getWhiteWinForfeitResult().setOnAction(e -> enterResult("+", "-"));
        setBlackWinForfeitResult(blackWinForfeitResult);
        getBlackWinForfeitResult().setOnAction(e -> enterResult("-", "+"));
        setDeleteRound(deleteRound);

        setGamesView(gamesView);
        getGamesView().setItems(getCurrentRound());
        getGamesView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                int rowIndex = getGamesView().getSelectionModel().selectedIndexProperty().get();
                setPairEnterCounter(rowIndex);
            }
        });

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
            private Game game;

            {
                hbox.getChildren().addAll(textField1, separator, textField2);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    game = getTableRow().getItem();
                    Player bye = getTournament().getPlayers().getBye();
                    Player halfbye = getTournament().getPlayers().getHalfbye();
                    Player unpaired = getTournament().getPlayers().getUnpaired();
                    Player black = game.getBlack();

                    if (black.equals(bye)) {
                        textField1.setText("1");
                        textField2.setText("0");
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else if (black.equals(halfbye)) {
                        textField1.setText("0.5");
                        textField2.setText("0.5");
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else if (black.equals(unpaired)) {
                        textField1.setText("0");
                        textField2.setText("1");
                        textField1.setDisable(true);
                        textField2.setDisable(true);
                    } else {
                        textField1.setText(Result.getResultString(game.getWhiteResult(), game.isForfeit()));
                        textField2.setText(Result.getResultString(game.getBlackResult(), game.isForfeit()));
                        textField1.setDisable(false);
                        textField2.setDisable(false);
                    }
                    textField1.setId("result" + getIndex() + "white");
                    textField2.setId("result" + getIndex() + "black");
                    textField1.textProperty().addListener(e -> {
                        if (textField1.isFocused()) {
                            if (textField1.getText().isEmpty()) {
                                game.setWhiteResult(null);
                                game.setForfeit(true);
                            } else {
                                Object[] objectsW = Result.getResultFromPoints(textField1.getText());
                                Object[] objects2 = Result.getResultFromPoints(textField2.getText());
                                Result resultW = (Result) objectsW[0];
                                Result resultB = (Result) objects2[0];
                                boolean forfeitW = (Boolean) objectsW[1];
                                boolean forfeitB = (Boolean) objects2[1];

                                if (resultW != null) {
                                    game.setWhiteResult(resultW);
                                    game.setForfeit(forfeitW || forfeitB);
                                }
                            }
                        }
                    });
                    textField2.textProperty().addListener(e -> {
                        if (textField2.isFocused()) {
                            if (textField2.getText().isEmpty()) {
                                game.setBlackResult(null);
                                game.setForfeit(true);
                            } else {
                                Object[] objectsW = Result.getResultFromPoints(textField1.getText());
                                Object[] objects2 = Result.getResultFromPoints(textField2.getText());
                                Result resultW = (Result) objectsW[0];
                                Result resultB = (Result) objects2[0];
                                boolean forfeitW = (Boolean) objectsW[1];
                                boolean forfeitB = (Boolean) objects2[1];

                                if (resultB != null) {
                                    game.setBlackResult(resultB);
                                    game.setForfeit(forfeitW || forfeitB);
                                }
                            }
                        }
                    });
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
            if (index >= 0) {
                if (index == last) {
                    if (roundsNumbersObs.size() > 1) {
                        getRoundsViewSelect().getSelectionModel().selectPrevious();
                        getTournament().getRoundsObs().remove(index);
                    } else {
                        getRoundsViewSelect().setValue(null);
                        getCurrentRound().clear();
                        getTournament().getRoundsObs().clear();
                        getGamesView().refresh();
                    }
                } else {
                    confirm("This will also remove subsequent rounds")
                            .thenAccept(result -> {
                                if (result) {
                                    if (roundsNumbersObs.size() > 1) {
                                        getRoundsViewSelect().getSelectionModel().selectPrevious();
                                        getTournament().getRoundsObs().remove(index, last + 1);
                                    } else {
                                        getRoundsViewSelect().setValue(null);
                                        getCurrentRound().clear();
                                        getTournament().getRoundsObs().clear();
                                        getGamesView().refresh();
                                    }
                                }
                            });
                }
            }
        });

        setEnginePairButton(enginePairButton);
        getEnginePairButton().setOnAction(e -> {
            if (getTournament().getRounds().size() < getTournament().getRoundsNumber() || getTournament().getSystem() != Tournament.TournamentSystem.SWISS) {
                try {
                    if (getTournament().getSystem() == Tournament.TournamentSystem.ROUND_ROBIN) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Number of replays");
                        dialog.setHeaderText("Please enter a number of replays:");
                        dialog.setContentText("Replays:");

                        dialog.showAndWait().ifPresent(result -> {
                            byte replays = Byte.parseByte(result);
                            int pairing = 0;
                            boolean success = true;
                            for (int i = 0; i < replays; i++) {
                                try {
                                    pairing += RoundRobinEngine.generatePairing(getTournament(), i % 2 == 1);
                                } catch (IOException | InterruptedException ex) {
                                    success = false;
                                    break;
                                }
                            }
                            if (success) {
                                info("Paired successfully\nGenerated " + pairing + " pairings");
                            } else {
                                error("An error occurred during pairing");
                            }
                            getTournament().setRoundsNumber((byte) (replays * Math.round(getTournament().getPlayersObs().size())));
                        });
                    } else {
                        int pairing = JavafoWrapper.generatePairing(getTournament(), false);
                        getRoundsViewSelect().getSelectionModel().selectLast();
                        info("Paired successfully\nGenerated " + pairing + " pairings");
                    }
                } catch (IOException | InterruptedException ex) {
                    error("An error occurred during pairing");
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            } else {
                error("Unable to pairing - number of rounds in tournament reached");
            }
        });
    }

    public void enterResult(String whiteResult, String blackResult) {
        TextField textField1 = (TextField) getGamesView().lookup("#result" + getPairEnterCounter() + "white");
        TextField textField2 = (TextField) getGamesView().lookup("#result" + getPairEnterCounter() + "black");
        if (textField1 == null || textField2 == null || textField1.isDisable() || textField2.isDisable()) {
            return;
        }
        textField1.requestFocus();
        textField1.setText(whiteResult);
        textField2.requestFocus();
        textField2.setText(blackResult);
        setPairEnterCounter(getPairEnterCounter() + 1);
        getGamesView().scrollTo(getPairEnterCounter() - 1);
        getGamesView().requestFocus();
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
        if (getTournament().getSystem() == Tournament.TournamentSystem.SWISS) {
            this.currentRound.sort(new PairingComparator(getTournament().getPlayersObs()));
        }
    }

    public Button getDeleteRound() {
        return deleteRound;
    }

    public void setDeleteRound(Button deleteRound) {
        this.deleteRound = deleteRound;
    }

    public int getPairEnterCounter() {
        return pairEnterCounter;
    }

    public void setPairEnterCounter(int pairEnterCounter) {

        this.pairEnterCounter = pairEnterCounter;
        try {
            getGamesView().getSelectionModel().select(pairEnterCounter);
        } catch (Exception ignored) {
        }
    }

}