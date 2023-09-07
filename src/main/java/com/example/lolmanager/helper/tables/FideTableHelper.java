package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.*;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class FideTableHelper {
    private Tournament tournament;
    private TableView<Player> rtgFideTable;
    private TableColumn<Player, String> rtgFideName;
    private TableColumn<Player, Integer> rtgFideId;
    private TableColumn<Player, Title> rtgFideTitle;
    private TableColumn<Player, Federation> rtgFideFed;
    private TableColumn<Player, Integer> rtgFideElo;
    private TableColumn<Player, Float> rtgFidePoints;
    private TableColumn<Player, Integer> rtgFideGames;
    private TableColumn<Player, Integer> rtgFideAverage;
    private TableColumn<Player, Float> rtgFideChg;
    private TableColumn<Player, Title> rtgFideNorm;

    public FideTableHelper(
            Tournament tournament, TableView<Player> rtgFideTable, TableColumn<Player, String> rtgFideName,
            TableColumn<Player, Integer> rtgFideId, TableColumn<Player, Title> rtgFideTitle,
            TableColumn<Player, Federation> rtgFideFed, TableColumn<Player, Integer> rtgFideElo,
            TableColumn<Player, Float> rtgFidePoints, TableColumn<Player, Integer> rtgFideGames,
            TableColumn<Player, Integer> rtgFideAverage, TableColumn<Player, Float> rtgFideChg,
            TableColumn<Player, Title> rtgFideNorm
    ) {
        setTournament(tournament);
        setRtgFideTable(rtgFideTable);
        setRtgFideName(rtgFideName);
        rtgFideName.setCellValueFactory(new PropertyValueFactory<>("name"));

        setRtgFideId(rtgFideId);
        rtgFideId.setCellValueFactory(new PropertyValueFactory<>("fideId"));
        setRtgFideTitle(rtgFideTitle);
        rtgFideTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        setRtgFideFed(rtgFideFed);
        rtgFideFed.setCellValueFactory(new PropertyValueFactory<>("federation"));

        setRtgFideElo(rtgFideElo);
        rtgFideElo.setCellValueFactory(new PropertyValueFactory<>("fideRating"));

        setRtgFidePoints(rtgFidePoints);
        rtgFidePoints.setCellValueFactory(new PropertyValueFactory<>("fidePoints"));

        setRtgFideGames(rtgFideGames);
        rtgFideGames.setCellValueFactory(new PropertyValueFactory<>("fidePlayedGamedNumber"));

        setRtgFideAverage(rtgFideAverage);
        rtgFideAverage.setCellValueFactory(new PropertyValueFactory<>("averageFideRating"));

        setRtgFideChg(rtgFideChg);
        rtgFideChg.setCellValueFactory(new PropertyValueFactory<>("fideChange"));

        setRtgFideNorm(rtgFideNorm);

        rtgFideTable.setItems(tournament.getPlayersObs());

        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            getRtgFideTable().refresh();
        });

    }


    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TableView<Player> getRtgFideTable() {
        return rtgFideTable;
    }

    public void setRtgFideTable(TableView<Player> rtgFideTable) {
        this.rtgFideTable = rtgFideTable;
    }

    public TableColumn<Player, String> getRtgFideName() {
        return rtgFideName;
    }

    public void setRtgFideName(TableColumn<Player, String> rtgFideName) {
        this.rtgFideName = rtgFideName;
    }

    public TableColumn<Player, Integer> getRtgFideId() {
        return rtgFideId;
    }

    public void setRtgFideId(TableColumn<Player, Integer> rtgFideId) {
        this.rtgFideId = rtgFideId;
    }

    public TableColumn<Player, Title> getRtgFideTitle() {
        return rtgFideTitle;
    }

    public void setRtgFideTitle(TableColumn<Player, Title> rtgFideTitle) {
        this.rtgFideTitle = rtgFideTitle;
    }

    public TableColumn<Player, Federation> getRtgFideFed() {
        return rtgFideFed;
    }

    public void setRtgFideFed(TableColumn<Player, Federation> rtgFideFed) {
        this.rtgFideFed = rtgFideFed;
    }

    public TableColumn<Player, Integer> getRtgFideElo() {
        return rtgFideElo;
    }

    public void setRtgFideElo(TableColumn<Player, Integer> rtgFideElo) {
        this.rtgFideElo = rtgFideElo;
    }

    public TableColumn<Player, Float> getRtgFidePoints() {
        return rtgFidePoints;
    }

    public void setRtgFidePoints(TableColumn<Player, Float> rtgFidePoints) {
        this.rtgFidePoints = rtgFidePoints;
    }

    public TableColumn<Player, Integer> getRtgFideGames() {
        return rtgFideGames;
    }

    public void setRtgFideGames(TableColumn<Player, Integer> rtgFideGames) {
        this.rtgFideGames = rtgFideGames;
    }

    public TableColumn<Player, Integer> getRtgFideAverage() {
        return rtgFideAverage;
    }

    public void setRtgFideAverage(TableColumn<Player, Integer> rtgFideAverage) {
        this.rtgFideAverage = rtgFideAverage;
    }

    public TableColumn<Player, Float> getRtgFideChg() {
        return rtgFideChg;
    }

    public void setRtgFideChg(TableColumn<Player, Float> rtgFideChg) {
        this.rtgFideChg = rtgFideChg;
    }

    public TableColumn<Player, Title> getRtgFideNorm() {
        return rtgFideNorm;
    }

    public void setRtgFideNorm(TableColumn<Player, Title> rtgFideNorm) {
        this.rtgFideNorm = rtgFideNorm;
    }

}
