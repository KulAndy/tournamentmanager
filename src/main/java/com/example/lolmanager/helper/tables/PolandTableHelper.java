package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.*;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class PolandTableHelper {

    private Tournament tournament;
    private TableView<Player> rtgPolTable;
    private TableColumn<Player, Integer> rtPolId;
    private TableColumn<Player, Title> rtPolTitle;
    private TableColumn<Player, String> rtPolName;
    private TableColumn<Player, Integer> rtPolGames;
    private TableColumn<Player, Float> rtPolPoints;
    private TableColumn<Player, Integer> rtPolAverage;
    private TableColumn<Player, Integer> rtPolPerformence;
    private TableColumn<Player, String> rtPolNorm;
    private TableColumn<Player, String> rtPolRemarks;

    public PolandTableHelper(
            Tournament tournament,
            TableView<Player> rtgPolTable,
            TableColumn<Player, Integer> rtPolId, TableColumn<Player, Title> rtPolTitle,
            TableColumn<Player, String> rtPolName, TableColumn<Player, Integer> rtPolGames,
            TableColumn<Player, Float> rtPolPoints, TableColumn<Player, Integer> rtPolAverage,
            TableColumn<Player, Integer> rtPolPerformance, TableColumn<Player, String> rtPolNorm,
            TableColumn<Player, String> rtPolRemarks
    ) {
        setTournament(tournament);
        setRtgPolTable(rtgPolTable);

        setRtPolId(rtPolId);
        rtPolId.setCellValueFactory(new PropertyValueFactory<>("localId"));
        setRtPolTitle(rtPolTitle);
        rtPolTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        setRtPolName(rtPolName);
        rtPolName.setCellValueFactory(new PropertyValueFactory<>("name"));
        setRtPolGames(rtPolGames);
        getRtPolGames().setCellValueFactory(new PropertyValueFactory<>("playedGamedNumber"));

        setRtPolPoints(rtPolPoints);
        getRtPolPoints().setCellValueFactory(new PropertyValueFactory<>("PZSzachPoints"));

        setRtPolAverage(rtPolAverage);
        getRtPolAverage().setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        setRtPolPerformance(rtPolPerformance);
        getRtPolPerformence().setCellValueFactory(new PropertyValueFactory<>("ratingPerformance"));

        setRtPolNorm(rtPolNorm);
        getRtPolNorm().setCellValueFactory(new PropertyValueFactory<>("playerNorm"));

        setRtPolRemarks(rtPolRemarks);

        getRtgPolTable().setItems(tournament.getPlayersObs());


        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            getRtgPolTable().refresh();
        });
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TableView<Player> getRtgPolTable() {
        return rtgPolTable;
    }

    public void setRtgPolTable(TableView<Player> rtgPolTable) {
        this.rtgPolTable = rtgPolTable;
    }

    public TableColumn<Player, Integer> getRtPolId() {
        return rtPolId;
    }

    public void setRtPolId(TableColumn<Player, Integer> rtPolId) {
        this.rtPolId = rtPolId;
    }

    public TableColumn<Player, Title> getRtPolTitle() {
        return rtPolTitle;
    }

    public void setRtPolTitle(TableColumn<Player, Title> rtPolTitle) {
        this.rtPolTitle = rtPolTitle;
    }

    public TableColumn<Player, String> getRtPolName() {
        return rtPolName;
    }

    public void setRtPolName(TableColumn<Player, String> rtPolName) {
        this.rtPolName = rtPolName;
    }

    public TableColumn<Player, Integer> getRtPolGames() {
        return rtPolGames;
    }

    public void setRtPolGames(TableColumn<Player, Integer> rtPolGames) {
        this.rtPolGames = rtPolGames;
    }

    public TableColumn<Player, Float> getRtPolPoints() {
        return rtPolPoints;
    }

    public void setRtPolPoints(TableColumn<Player, Float> rtPolPoints) {
        this.rtPolPoints = rtPolPoints;
    }

    public TableColumn<Player, Integer> getRtPolAverage() {
        return rtPolAverage;
    }

    public void setRtPolAverage(TableColumn<Player, Integer> rtPolAverage) {
        this.rtPolAverage = rtPolAverage;
    }

    public TableColumn<Player, Integer> getRtPolPerformence() {
        return rtPolPerformence;
    }

    public void setRtPolPerformance(TableColumn<Player, Integer> rtPolPerformence) {
        this.rtPolPerformence = rtPolPerformence;
    }

    public TableColumn<Player, String> getRtPolNorm() {
        return rtPolNorm;
    }

    public void setRtPolNorm(TableColumn<Player, String> rtPolNorm) {
        this.rtPolNorm = rtPolNorm;
    }

    public TableColumn<Player, String> getRtPolRemarks() {
        return rtPolRemarks;
    }

    public void setRtPolRemarks(TableColumn<Player, String> rtPolRemarks) {
        this.rtPolRemarks = rtPolRemarks;
    }


}
