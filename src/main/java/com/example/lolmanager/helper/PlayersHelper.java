package com.example.lolmanager.helper;

import com.example.lolmanager.comparator.StartListComparator;
import com.example.lolmanager.helper.players.NewPlayerHelper;
import com.example.lolmanager.helper.players.PlayerCardHelper;
import com.example.lolmanager.helper.players.PlayersSortHelper;
import com.example.lolmanager.helper.players.StartListHelper;
import com.example.lolmanager.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PlayersHelper {
    private Tournament tournament;
    private StartListHelper startListHelper;
    private PlayersSortHelper playersSortHelper;
    private NewPlayerHelper newPlayerHelper;
    private PlayerCardHelper playerCardHelper;

    public PlayersHelper(
            Tournament tournament,
            Button correctFide, Button correctPl,
            TableView<Player> playersListTable,
            TableColumn<Player, Integer> startNoCol, TableColumn<Player, Title> titleCol, TableColumn<Player, String> nameCol, TableColumn<Player, Federation> fedCol,
            TableColumn<Player, Integer> fideCol, TableColumn<Player, Integer> localCol, TableColumn<Player, String> clubCol, TableColumn<Player, Integer> localIdCol,
            TableColumn<Player, Integer> fideIdCol, TableColumn<Player, String> remarksCol, TableColumn<Player, Void> deleteCol,
            ComboBox<StartListComparator.SortCriteria> criteria1,
            ComboBox<StartListComparator.SortCriteria> criteria2,
            ComboBox<StartListComparator.SortCriteria> criteria3,
            ComboBox<StartListComparator.SortCriteria> criteria4,
            ComboBox<StartListComparator.SortCriteria> criteria5,
            Button applySortButton,
            ComboBox<Player> playerSelect,
            ComboBox<Federation> fedSelect, ComboBox<String> stateSelect, TextField playerNameField,
            ComboBox<Title> playerTitleSelect, TextField localRtgField, TextField FIDERtgField,
            TextField clubField, TextField dayOfBirth, TextField monthOfBirth, TextField yearOfBirth,
            ComboBox<Player.Sex> sexSelect, TextField mailField, ComboBox<Short> phonePrefixSelect,
            TextField phoneNumber, TextField localIDField, TextField FIDEIDField, TextField remarksField,
            Button addPlayerButton, Button updatePlayerBth, Button clearPlayerButton, Button addClearPlayerButton,
            Button insertFromList, ListView<Player> newPlayerHint,
            ComboBox<Player> playerCardSelect,
            Label playerCardName, Label playerCardTB1, Label playerCardTB1Value,
            Label playerCardTB2, Label playerCardTB2Value, Label playerCardTB3, Label playerCardTB3Value,
            Label playerCardTB4, Label playerCardTB4Value, Label playerCardTB5, Label playerCardTB5Value,
            Label playerCardElo, Label playerCardEloValue, Label playerCardPZSzach, Label playerCardPZSzachValue,
            TableView<Game> playerCardGames, TableColumn<Game, Integer> playerCardOppRound,
            TableColumn<Game, Player.Color> playerCardOppColor, TableColumn<Game, String> playerCardOppResult,
            TableColumn<Game, Title> playerCardOppTitle, TableColumn<Game, String> playerCardOppName,
            TableColumn<Game, Integer> playerCardOppRtg, GridPane cardGrid

    ) {
        setTournament(tournament);
        setStartListHelper(new StartListHelper(
                tournament,
                correctFide, correctPl,
                playersListTable,
                startNoCol, titleCol, nameCol, fedCol, fideCol,
                localCol, clubCol, localIdCol, fideIdCol, remarksCol, deleteCol
        ));

        setPlayersSortHelper(
                new PlayersSortHelper(
                        tournament,
                        criteria1, criteria2, criteria3, criteria4, criteria5, applySortButton
                )
        );

        setNewPlayerHelper(
                new NewPlayerHelper(
                        tournament,
                        playerSelect,
                        fedSelect, stateSelect, playerNameField,
                        playerTitleSelect, localRtgField, FIDERtgField,
                        clubField, dayOfBirth, monthOfBirth, yearOfBirth,
                        sexSelect, mailField, phonePrefixSelect,
                        phoneNumber, localIDField, FIDEIDField, remarksField,
                        addPlayerButton, updatePlayerBth, clearPlayerButton, addClearPlayerButton,
                        insertFromList, newPlayerHint
                )
        );
        updatePlayerBth.addEventHandler(ActionEvent.ACTION, e -> playersListTable.refresh());

        setPlayerCardHelper(
                new PlayerCardHelper(
                        tournament,
                        playerCardSelect,
                        playerCardName, playerCardTB1, playerCardTB1Value,
                        playerCardTB2, playerCardTB2Value, playerCardTB3, playerCardTB3Value,
                        playerCardTB4, playerCardTB4Value, playerCardTB5, playerCardTB5Value,
                        playerCardElo, playerCardEloValue, playerCardPZSzach, playerCardPZSzachValue,
                        playerCardGames, playerCardOppRound, playerCardOppColor, playerCardOppResult,
                        playerCardOppTitle, playerCardOppName, playerCardOppRtg, cardGrid

                )
        );
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public PlayersSortHelper getPlayersSortHelper() {
        return playersSortHelper;
    }

    public void setPlayersSortHelper(PlayersSortHelper playersSortHelper) {
        this.playersSortHelper = playersSortHelper;
    }

    public StartListHelper getStartListHelper() {
        return startListHelper;
    }

    public void setStartListHelper(StartListHelper startListHelper) {
        this.startListHelper = startListHelper;
    }

    public NewPlayerHelper getNewPlayerHelper() {
        return newPlayerHelper;
    }

    public void setNewPlayerHelper(NewPlayerHelper newPlayerHelper) {
        this.newPlayerHelper = newPlayerHelper;
    }

    public PlayerCardHelper getPlayerCardHelper() {
        return playerCardHelper;
    }

    public void setPlayerCardHelper(PlayerCardHelper playerCardHelper) {
        this.playerCardHelper = playerCardHelper;
    }
}
