package com.example.lolmanager.helper;

import com.example.lolmanager.comparator.StartListComparator;
import com.example.lolmanager.helper.players.PlayersSortHelper;
import com.example.lolmanager.helper.players.StartListHelper;
import com.example.lolmanager.helper.players.NewPlayerHelper;
import com.example.lolmanager.model.Federation;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.scene.control.*;

public class PlayersHelper {
    private Tournament tournament;
    private StartListHelper startListHelper;
    private PlayersSortHelper playersSortHelper;
    private NewPlayerHelper newPlayerHelper;

    public PlayersHelper(
            Tournament tournament, TableView<Player> playersListTable,
            TableColumn<Player, Integer> startNoCol, TableColumn<Player, Title> titleCol, TableColumn<Player, String> nameCol, TableColumn<Player, Federation> fedCol,
            TableColumn<Player, Integer> fideCol, TableColumn<Player, Integer> localCol, TableColumn<Player, String> clubCol, TableColumn<Player, Integer> localIdCol,
            TableColumn<Player, Integer> fideIdCol, TableColumn<Player, String> remarksCol, TableColumn<Player, Void> deleteCol,
            ComboBox<StartListComparator.SortCriteria> criteria1,
            ComboBox<StartListComparator.SortCriteria> criteria2,
            ComboBox<StartListComparator.SortCriteria> criteria3,
            ComboBox<StartListComparator.SortCriteria> criteria4,
            ComboBox<StartListComparator.SortCriteria> criteria5,
            Button applySortButton,
            ComboBox<Federation> fedSelect, ComboBox<String> stateSelect, TextField playerNameField,
            ComboBox<Title> playerTitleSelect, TextField localRtgField, TextField FIDERtgField,
            TextField clubField, TextField dayOfBirth, TextField monthOfBirth, TextField yearOfBirth,
            ComboBox<Player.Sex> sexSelect, TextField mailField, ComboBox<Short> phonePrefixSelect,
            TextField phoneNumber, TextField localIDField, TextField FIDEIDField, TextField remarksField,
            Button addPlayerButton, Button clearPlayerButton, Button addClearPlayerButton,
            Button insertFromList, ListView<Player> newPlayerHint

    ) {
        setTournament(tournament);
        setStartListHelper(new StartListHelper(
                tournament, playersListTable,
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
                        fedSelect, stateSelect, playerNameField,
                        playerTitleSelect, localRtgField, FIDERtgField,
                        clubField, dayOfBirth, monthOfBirth, yearOfBirth,
                        sexSelect, mailField, phonePrefixSelect,
                        phoneNumber, localIDField, FIDEIDField, remarksField,
                        addPlayerButton, clearPlayerButton, addClearPlayerButton,
                        insertFromList, newPlayerHint
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

}
