package com.example.tournamentmanager.helper.round;

import com.example.tournamentmanager.model.Game;
import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Tournament;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;

import java.util.ArrayList;

import static com.example.tournamentmanager.helper.GeneralHelper.*;

public class ManualPairingHelper {
    private final StringProperty whiteStart = new SimpleStringProperty("");
    private final StringProperty blackStart = new SimpleStringProperty("");
    private Tournament tournament;
    private ComboBox<Integer> roundUpdateSelect;
    private TextField whiteSearch;
    private TextField blackSearch;
    private ListView<Player> whiteList;
    private ListView<Player> blackList;
    private CheckBox autoColorCheckbox;
    private Button pairRestButton;
    private Button pairButton;
    private Button whithdrawButton;
    private Button byePairButton;
    private Button halfByePairButton;
    private Button clearManualButton;
    private Button unpairButton;
    private Button swapColorPairButton;
    private Button applyManualButton;
    private ListView<Game> pairsList;
    private ObservableList<Player> paired = FXCollections.observableArrayList();
    private FilteredList<Player> whitePairedFilter;
    private FilteredList<Player> blackPairedFilter;
    private ObservableList<Integer> roundsNumbersObs = FXCollections.observableArrayList(1);
    private ObservableList<Game> manualRound = FXCollections.observableArrayList();

    public ManualPairingHelper(
            Tournament tournament,
            ComboBox<Integer> roundUpdateSelect, TextField whiteSearch, TextField blackSearch,
            ListView<Player> whiteList, ListView<Player> blackList, CheckBox autoColorCheckbox, Button pairRestButton,
            Button pairButton, Button whithdrawButton, Button byePairButton, Button halfByePairButton, Button clearManualButton,
            Button unpairButton, Button swapColorPairButton,
            Button applyManualButton, ListView<Game> pairsList
    ) {
        setTournament(tournament);
        setPairsList(pairsList);
        setRoundUpdateSelect(roundUpdateSelect);
        setWhiteSearch(whiteSearch);
        setBlackSearch(blackSearch);
        setWhiteList(whiteList);
        setBlackList(blackList);
        setAutoColorCheckbox(autoColorCheckbox);
        setPairRestButton(pairRestButton);
        setPairButton(pairButton);
        setWhithdrawButton(whithdrawButton);
        setByePairButton(byePairButton);
        setHalfByePairButton(halfByePairButton);
        setClearManualButton(clearManualButton);
        setUnpairButton(unpairButton);
        setSwapColorPairButton(swapColorPairButton);
        setApplyManualButton(applyManualButton);
        setWhitePairedFilter(new FilteredList<>(getTournament().getPlayersObs(), item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(whiteStart.get().toLowerCase().trim())));
        setBlackPairedFilter(new FilteredList<>(getTournament().getPlayersObs(), item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(blackStart.get().toLowerCase().trim())));

        getPairsList().setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

        getPairsList().setItems(manualRound);
        getPairsList().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Game item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    String white = String.format("%-4d %-3s %35s", item.getWhite().getFideRating(), item.getWhite().getTitle(), item.getWhite().getName());
                    String black =
                            item.getBlack() == getTournament().getPlayers().getBye()
                                    || item.getBlack() == getTournament().getPlayers().getHalfbye()
                                    || item.getBlack() == getTournament().getPlayers().getUnpaired()
                                    ? String.format("%-35s", item.getBlack().getName())
                                    : String.format("%-35s %-3s %-4d", item.getBlack().getName(), item.getBlack().getTitle(), item.getBlack().getFideRating());
                    String text = String.format("%3d.  %s - %s ", getIndex() + 1, white, black);
                    setText(text);
                } else {
                    setText(null);
                }
            }
        });
        getRoundUpdateSelect().setItems(getRoundsNumbersObs());
        getRoundUpdateSelect().valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            try {
                setManualRound(FXCollections.observableArrayList(getTournament().getRoundsObs().get(newValue - 1)));
            } catch (Exception e) {
                setManualRound(FXCollections.observableArrayList());
            }
            getPairsList().setItems(getManualRound());
            getPaired().clear();
            for (Game game : getManualRound()) {
                Player white = game.getWhite();
                Player black = game.getBlack();
                Player bye = getTournament().getPlayers().getBye();
                Player halfbye = getTournament().getPlayers().getHalfbye();
                if (white != null && white != bye && white != halfbye) {
                    getPaired().add(white);
                }
                if (black != null && black != bye && black != halfbye && white != black) {
                    getPaired().add(black);
                }
            }
        });

        getRoundUpdateSelect().setValue(1);
        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            ArrayList<Integer> rounds = new ArrayList<>();
            for (int i = 1; i <= getTournament().getRoundsObs().size() + 1 && i <= getTournament().getRoundsNumber(); i++) {
                rounds.add(i);
            }
            setRoundsNumbersObs(FXCollections.observableArrayList(rounds));
            getRoundUpdateSelect().setItems(getRoundsNumbersObs());
        });


        getWhiteSearch().textProperty().bindBidirectional(whiteStart);
        getBlackSearch().textProperty().bindBidirectional(blackStart);
        getWhiteSearch().textProperty().addListener(e -> getWhitePairedFilter().setPredicate(item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(whiteStart.get().toLowerCase().trim())));
        getBlackSearch().textProperty().addListener(e -> getBlackPairedFilter().setPredicate(item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(blackStart.get().toLowerCase().trim())));

        getPairButton().setOnAction(e -> {
            Player white = getWhiteList().getSelectionModel().getSelectedItem();
            Player black = getBlackList().getSelectionModel().getSelectedItem();
            if (white == black) {
                error("Player cannot be paired with himself !!!");
            } else if (white == null || black == null) {
                warning("Two players are needed for pairing");
            } else {
                getManualRound().add(new Game(white, black));
                getPaired().add(white);
                getPaired().add(black);
            }
            getManualRound().sort(getTournament().getPairingComparator());
        });

        getWhithdrawButton().setOnAction(e -> {
            Player white = getWhiteList().getSelectionModel().getSelectedItem();
            Player black = getBlackList().getSelectionModel().getSelectedItem();
            if (white != null) {
                getPaired().add(white);
                getManualRound().add(new Game(white, getTournament().getPlayers().getUnpaired()));
            }
            if (black != null && black != white) {
                getPaired().add(black);
                getManualRound().add(new Game(black, getTournament().getPlayers().getUnpaired()));
            }
            getManualRound().sort(getTournament().getPairingComparator());

        });
        getByePairButton().setOnAction(e -> {
            Player white = getWhiteList().getSelectionModel().getSelectedItem();
            Player black = getBlackList().getSelectionModel().getSelectedItem();
            if (white != null) {
                getPaired().add(white);
                getManualRound().add(new Game(white, getTournament().getPlayers().getBye()));
            }
            if (black != null && black != white) {
                getPaired().add(black);
                getManualRound().add(new Game(black, getTournament().getPlayers().getBye()));
            }
            getManualRound().sort(getTournament().getPairingComparator());
            getWhiteSearch().setText("");
            getBlackSearch().setText("");
        });
        getHalfByePairButton().setOnAction(e -> {
            Player white = getWhiteList().getSelectionModel().getSelectedItem();
            Player black = getBlackList().getSelectionModel().getSelectedItem();
            if (white != null) {
                getPaired().add(white);
                getManualRound().add(new Game(white, getTournament().getPlayers().getHalfbye()));
            }
            if (black != null && black != white) {
                getPaired().add(black);
                getManualRound().add(new Game(black, getTournament().getPlayers().getHalfbye()));
            }
            getManualRound().sort(getTournament().getPairingComparator());
        });

        getClearManualButton().setOnAction(e -> {
            getWhiteList().getSelectionModel().clearSelection();
            getBlackList().getSelectionModel().clearSelection();
            getPairsList().getSelectionModel().clearSelection();
        });
        getUnpairButton().setOnAction(e -> {
            Game game = pairsList.getSelectionModel().getSelectedItem();
            getManualRound().remove(game);
            getPaired().removeAll(game.getWhite(), game.getBlack());
        });

        getSwapColorPairButton().setOnAction(e -> {
            Game game = getPairsList().getSelectionModel().getSelectedItem();
            if (game != null) {

                Player black = game.getBlack();
                if (black != getTournament().getPlayers().getBye() && black != getTournament().getPlayers().getHalfbye() && black != getTournament().getPlayers().getUnpaired()) {
                    game.swapPlayers();
                } else {
                    warning("Cannot swap colors for bye/halfbye");
                }
                getPairsList().getSelectionModel().clearSelection();
            }
        });
        getApplyManualButton().setOnAction(e -> {
            int currentRound = getRoundUpdateSelect().getValue();
            ArrayList<Game> pom = new ArrayList<>(getManualRound());
            if (pom.isEmpty()) {
                warning("Round cannot be empty");
            } else if (getPaired().size() < getTournament().getPlayersObs().size()) {
                confirm("Not every player was paired")
                        .thenAccept(result -> {
                            if (result) {
                                if (getTournament().getRoundsObs().size() >= currentRound) {
                                    getTournament().getRoundsObs().set(currentRound - 1, pom);
                                } else {
                                    getTournament().getRoundsObs().add(pom);
                                }
                            }
                        });
            } else {
                if (getTournament().getRoundsObs().size() >= currentRound) {
                    getTournament().getRoundsObs().set(currentRound - 1, pom);
                } else {
                    getTournament().getRoundsObs().add(pom);
                }
            }
        });


        getPaired().addListener((ListChangeListener<Player>) change -> {
            getWhitePairedFilter().setPredicate(item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(whiteStart.get().toLowerCase().trim()));
            getBlackPairedFilter().setPredicate(item -> !getPaired().contains(item) && item.getName().toLowerCase().startsWith(blackStart.get().toLowerCase().trim()));
        });

        getWhiteList().setItems(getWhitePairedFilter());
        getWhiteList().setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");
        getWhiteList().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    int index = getTournament().getPlayersObs().indexOf(item) + 1;
                    String text = String.format("%3d.  %-50s %3s %4d ", index, item.getName(), item.getTitle(), item.getFideRating());
                    setText(text);
                } else {
                    setText(null);
                }
            }
        });

        getBlackList().setItems(getBlackPairedFilter());
        getBlackList().setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");
        getBlackList().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    int index = getTournament().getPlayersObs().indexOf(item) + 1;
                    String text = String.format("%3d.  %-50s %3s %4d ", index, item.getName(), item.getTitle(), item.getFideRating());
                    setText(text);
                } else {
                    setText(null);
                }
            }
        });
    }

    public ObservableList<Integer> getRoundsNumbersObs() {
        return roundsNumbersObs;
    }

    public void setRoundsNumbersObs(ObservableList<Integer> roundsNumbersObs) {
        this.roundsNumbersObs = roundsNumbersObs;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public ComboBox<Integer> getRoundUpdateSelect() {
        return roundUpdateSelect;
    }

    public void setRoundUpdateSelect(ComboBox<Integer> roundUpdateSelect) {
        this.roundUpdateSelect = roundUpdateSelect;
    }

    public TextField getWhiteSearch() {
        return whiteSearch;
    }

    public void setWhiteSearch(TextField whiteSearch) {
        this.whiteSearch = whiteSearch;
    }

    public TextField getBlackSearch() {
        return blackSearch;
    }

    public void setBlackSearch(TextField blackSearch) {
        this.blackSearch = blackSearch;
    }

    public ListView<Player> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(ListView<Player> whiteList) {
        this.whiteList = whiteList;
    }

    public ListView<Player> getBlackList() {
        return blackList;
    }

    public void setBlackList(ListView<Player> blackList) {
        this.blackList = blackList;
    }

    public CheckBox getAutoColorCheckbox() {
        return autoColorCheckbox;
    }

    public void setAutoColorCheckbox(CheckBox autoColorCheckbox) {
        this.autoColorCheckbox = autoColorCheckbox;
    }

    public Button getPairRestButton() {
        return pairRestButton;
    }

    public void setPairRestButton(Button pairRestButton) {
        this.pairRestButton = pairRestButton;
    }

    public Button getPairButton() {
        return pairButton;
    }

    public void setPairButton(Button pairButton) {
        this.pairButton = pairButton;
    }

    public Button getWhithdrawButton() {
        return whithdrawButton;
    }

    public void setWhithdrawButton(Button whithdrawButton) {
        this.whithdrawButton = whithdrawButton;
    }

    public Button getByePairButton() {
        return byePairButton;
    }

    public void setByePairButton(Button byePairButton) {
        this.byePairButton = byePairButton;
    }

    public Button getClearManualButton() {
        return clearManualButton;
    }

    public void setClearManualButton(Button clearManualButton) {
        this.clearManualButton = clearManualButton;
    }

    public Button getUnpairButton() {
        return unpairButton;
    }

    public void setUnpairButton(Button unpairButton) {
        this.unpairButton = unpairButton;
    }

    public Button getSwapColorPairButton() {
        return swapColorPairButton;
    }

    public void setSwapColorPairButton(Button swapColorPairButton) {
        this.swapColorPairButton = swapColorPairButton;
    }

    public Button getApplyManualButton() {
        return applyManualButton;
    }

    public void setApplyManualButton(Button applyManualButton) {
        this.applyManualButton = applyManualButton;
    }

    public ListView<Game> getPairsList() {
        return pairsList;
    }

    public void setPairsList(ListView<Game> pairsList) {
        this.pairsList = pairsList;
    }

    public FilteredList<Player> getBlackPairedFilter() {
        return blackPairedFilter;
    }

    public void setBlackPairedFilter(FilteredList<Player> blackPairedFilter) {
        this.blackPairedFilter = blackPairedFilter;
    }

    public ObservableList<Game> getManualRound() {
        return manualRound;
    }

    public void setManualRound(ObservableList<Game> manualRound) {
        this.manualRound = manualRound;
    }

    public Button getHalfByePairButton() {
        return halfByePairButton;
    }

    public void setHalfByePairButton(Button halfByePairButton) {
        this.halfByePairButton = halfByePairButton;
    }

    public ObservableList<Player> getPaired() {
        return paired;
    }

    public void setPaired(ObservableList<Player> paired) {
        this.paired = paired;
    }

    public FilteredList<Player> getWhitePairedFilter() {
        return whitePairedFilter;
    }

    public void setWhitePairedFilter(FilteredList<Player> whitePairedFilter) {
        this.whitePairedFilter = whitePairedFilter;
    }
}
