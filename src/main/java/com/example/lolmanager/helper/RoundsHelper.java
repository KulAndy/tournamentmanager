package com.example.lolmanager.helper;

import com.example.lolmanager.helper.players.WithdrawHelper;
import com.example.lolmanager.helper.round.AllRoundsViewerHelper;
import com.example.lolmanager.helper.round.ManualPairingHelper;
import com.example.lolmanager.helper.round.ResultEnterHelper;
import com.example.lolmanager.model.Game;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Tournament;
import com.example.lolmanager.model.Withdraw;
import javafx.scene.control.*;

public class RoundsHelper {
    private ManualPairingHelper manualPairingHelper;
    private ResultEnterHelper resultEnterHelper;
    private AllRoundsViewerHelper allRoundsViewerHelper;
    private WithdrawHelper withdrawHelper;

    public RoundsHelper
            (
                    Tournament tournament,
                    ComboBox<Integer> roundUpdateSelect, TextField whiteSearch, TextField blackSearch,
                    ListView<Player> whiteList, ListView<Player> blackList, CheckBox autoColorCheckbox, Button pairRestButton,
                    Button pairButton, Button whithdrawButton, Button byePairButton, Button halfByePairButton, Button clearManualButton,
                    Button unpairButton, Button swapColorPairButton,
                    Button applyManualButton, ListView<Game> pairsList,
                    ComboBox<Integer> roundsViewSelect, Button firstRound, Button previousRound, Button nextRound, Button lastRound,
                    Button whiteWinResult, Button drawResult, Button blackWinResult, Button whiteWinForfeitResult, Button blackWinForfeitResult,
                    TableView<Game> gamesView, TableColumn<Game, Integer> leftBoardNo, TableColumn<Game, Float> whitePoints,
                    TableColumn<Game, Integer> whiteRating, TableColumn<Game, String> whitePlayer, TableColumn<Game, Void> gameResult,
                    TableColumn<Game, String> blackPlayer, TableColumn<Game, Integer> blackRating, TableColumn<Game, Float> blackPoints,
                    TableColumn<Game, Integer> rightBoardNo, Button deleteRound, Button enginePairButton,
                    ScrollPane allRoundsScroll,
                    ComboBox<Player> withdrawPlayerSelect, ComboBox<Withdraw.WithdrawType> withdrawTypeSelect,
                    TextField withdrawRound, Button acceptWithdrawButton, TableView<Withdraw> withdrawTable,
                    TableColumn<Withdraw, Integer> withdrawNoCol, TableColumn<Withdraw, String> withdrawNameCol,
                    TableColumn<Withdraw, Withdraw.WithdrawType> withdrawTypeCol, TableColumn<Withdraw, Byte> withdrawRoundCol,
                    TableColumn<Withdraw, Void> withdrawBackCol

            ) {
        setManualPairingHelper(new ManualPairingHelper(
                tournament, roundUpdateSelect, whiteSearch, blackSearch, whiteList, blackList,
                autoColorCheckbox, pairRestButton, pairButton, whithdrawButton, byePairButton, halfByePairButton,
                clearManualButton, unpairButton, swapColorPairButton,
                applyManualButton, pairsList
        ));
        setResultEnterHelper(new ResultEnterHelper(
                tournament,
                roundsViewSelect, firstRound, previousRound, nextRound, lastRound, whiteWinResult, drawResult,
                blackWinResult, whiteWinForfeitResult, blackWinForfeitResult, gamesView,
                leftBoardNo, whitePoints, whiteRating, whitePlayer, gameResult, blackPlayer,
                blackRating, blackPoints, rightBoardNo, deleteRound, enginePairButton
        ));

        setAllRoundsViewerHelper(new AllRoundsViewerHelper(tournament, allRoundsScroll));

        setWithdrawHelper(new WithdrawHelper(tournament, withdrawPlayerSelect,  withdrawTypeSelect, withdrawRound, acceptWithdrawButton, withdrawTable,
                withdrawNoCol, withdrawNameCol, withdrawTypeCol, withdrawRoundCol, withdrawBackCol
        ));
    }

    public ManualPairingHelper getManualPairingHelper() {
        return manualPairingHelper;
    }

    public void setManualPairingHelper(ManualPairingHelper manualPairingHelper) {
        this.manualPairingHelper = manualPairingHelper;
    }

    public ResultEnterHelper getResultEnterHelper() {
        return resultEnterHelper;
    }

    public void setResultEnterHelper(ResultEnterHelper resultEnterHelper) {
        this.resultEnterHelper = resultEnterHelper;
    }

    public AllRoundsViewerHelper getAllRoundsViewerHelper() {
        return allRoundsViewerHelper;
    }

    public void setAllRoundsViewerHelper(AllRoundsViewerHelper allRoundsViewerHelper) {
        this.allRoundsViewerHelper = allRoundsViewerHelper;
    }

    public WithdrawHelper getWithdrawHelper() {
        return withdrawHelper;
    }

    public void setWithdrawHelper(WithdrawHelper withdrawHelper) {
        this.withdrawHelper = withdrawHelper;
    }
}
