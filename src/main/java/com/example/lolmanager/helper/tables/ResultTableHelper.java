package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ResultTableHelper {
    private Tournament tournament;
    private CheckBox resultFiltered;
    private ComboBox resultFilter;
    private TableView<Player> resultsTable;
    private TableColumn<Player, Integer> resultPlace;
    private TableColumn<Player, Integer> resultStartNo;
    private TableColumn<Player, Title> resultTitle;
    private TableColumn<Player, String> resultName;
    private TableColumn<Player, Integer> resultElo;
    private TableColumn<Player, Integer> resultLocal;
    private TableColumn<Player, Federation> resultFed;
    private TableColumn<Player, Float> resultPoints;
    private TableColumn<Player, Float> resultBuchCut;
    private TableColumn<Player, Float> resultBuch;
    private TableColumn<Player, Float> resultBerger;
    private TableColumn<Player, Float> resultProgress;
    private SortedList<Player> sortedPlayers;

    public ResultTableHelper(
            Tournament tournament,
            CheckBox resultFiltered, ComboBox resultFilter, TableView<Player> resultsTable,
            TableColumn<Player, Integer> resultPlace, TableColumn<Player, Integer> resultStartNo,
            TableColumn<Player, Title> resultTitle, TableColumn<Player, String> resultName,
            TableColumn<Player, Integer> resultElo, TableColumn<Player, Integer> resultLocal,
            TableColumn<Player, Federation> resultFed, TableColumn<Player, Float> resultPoints,
            TableColumn<Player, Float> resultBuchCut, TableColumn<Player, Float> resultBuch,
            TableColumn<Player, Float> resultBerger, TableColumn<Player, Float> resultProgress
    ) {
        setTournament(tournament);
        setResultFiltered(resultFiltered);
        setResultFilter(resultFilter);

        setResultsTable(resultsTable);

        setResultPlace(resultPlace);
        getResultPlace().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getResultsTable().getItems().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });


        setResultStartNo(resultStartNo);
        getResultStartNo().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getTournament().getPlayersObs().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });

        setResultTitle(resultTitle);
        resultTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        setResultName(resultName);
        resultName.setCellValueFactory(new PropertyValueFactory<>("name"));

        setResultElo(resultElo);
        resultElo.setCellValueFactory(new PropertyValueFactory<>("fideRating"));

        setResultLocal(resultLocal);
        resultLocal.setCellValueFactory(new PropertyValueFactory<>("localRating"));

        setResultFed(resultFed);
        resultFed.setCellValueFactory(new PropertyValueFactory<>("federation"));

        setResultPoints(resultPoints);
        resultPoints.setCellValueFactory(new PropertyValueFactory<>("points"));

        setResultBuchCut(resultBuchCut);
        resultBuchCut.setCellValueFactory(new PropertyValueFactory<>("bucholzCut1"));

        setResultBuch(resultBuch);
        resultBuch.setCellValueFactory(new PropertyValueFactory<>("bucholz"));

        setResultBerger(resultBerger);
        resultBerger.setCellValueFactory(new PropertyValueFactory<>("berger"));

        setResultProgress(resultProgress);
        resultProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));

        SortedList<Player> sortedPlayersTmp = new SortedList<>(getTournament().getPlayersObs(), getTournament().getResultsComparator());
        setSortedPlayers(sortedPlayersTmp);

        getResultsTable().setItems(getSortedPlayers());

        getTournament().getPlayersObs().addListener((ListChangeListener<? super Player>) change -> {
            SortedList<Player> sortedPlayersTmp2 = new SortedList<>(getTournament().getPlayersObs(), getTournament().getResultsComparator());
            setSortedPlayers(sortedPlayersTmp2);
            getResultsTable().refresh();
        });

        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            SortedList<Player> sortedPlayersTmp2 = new SortedList<>(getTournament().getPlayersObs(), getTournament().getResultsComparator());
            setSortedPlayers(sortedPlayersTmp2);
            getResultsTable().refresh();
        });

    }

    public SortedList<Player> getSortedPlayers() {
        return sortedPlayers;
    }

    public void setSortedPlayers(SortedList<Player> sortedPlayers) {
        this.sortedPlayers = sortedPlayers;
    }


    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public CheckBox getResultFiltered() {
        return resultFiltered;
    }

    public void setResultFiltered(CheckBox resultFiltered) {
        this.resultFiltered = resultFiltered;
    }

    public ComboBox getResultFilter() {
        return resultFilter;
    }

    public void setResultFilter(ComboBox resultFilter) {
        this.resultFilter = resultFilter;
    }

    public TableView<Player> getResultsTable() {
        return resultsTable;
    }

    public void setResultsTable(TableView<Player> resultsTable) {
        this.resultsTable = resultsTable;
    }

    public TableColumn<Player, Integer> getResultPlace() {
        return resultPlace;
    }

    public void setResultPlace(TableColumn<Player, Integer> resultPlace) {
        this.resultPlace = resultPlace;
    }

    public TableColumn<Player, Integer> getResultStartNo() {
        return resultStartNo;
    }

    public void setResultStartNo(TableColumn<Player, Integer> resultStartNo) {
        this.resultStartNo = resultStartNo;
    }

    public TableColumn<Player, Title> getResultTitle() {
        return resultTitle;
    }

    public void setResultTitle(TableColumn<Player, Title> resultTitle) {
        this.resultTitle = resultTitle;
    }

    public TableColumn<Player, String> getResultName() {
        return resultName;
    }

    public void setResultName(TableColumn<Player, String> resultName) {
        this.resultName = resultName;
    }

    public TableColumn<Player, Integer> getResultElo() {
        return resultElo;
    }

    public void setResultElo(TableColumn<Player, Integer> resultElo) {
        this.resultElo = resultElo;
    }

    public TableColumn<Player, Integer> getResultLocal() {
        return resultLocal;
    }

    public void setResultLocal(TableColumn<Player, Integer> resultLocal) {
        this.resultLocal = resultLocal;
    }

    public TableColumn<Player, Federation> getResultFed() {
        return resultFed;
    }

    public void setResultFed(TableColumn<Player, Federation> resultFed) {
        this.resultFed = resultFed;
    }

    public TableColumn<Player, Float> getResultPoints() {
        return resultPoints;
    }

    public void setResultPoints(TableColumn<Player, Float> resultPoints) {
        this.resultPoints = resultPoints;
    }

    public TableColumn<Player, Float> getResultBuchCut() {
        return resultBuchCut;
    }

    public void setResultBuchCut(TableColumn<Player, Float> resultBuchCut) {
        this.resultBuchCut = resultBuchCut;
    }

    public TableColumn<Player, Float> getResultBuch() {
        return resultBuch;
    }

    public void setResultBuch(TableColumn<Player, Float> resultBuch) {
        this.resultBuch = resultBuch;
    }

    public TableColumn<Player, Float> getResultBerger() {
        return resultBerger;
    }

    public void setResultBerger(TableColumn<Player, Float> resultBerger) {
        this.resultBerger = resultBerger;
    }

    public TableColumn<Player, Float> getResultProgress() {
        return resultProgress;
    }

    public void setResultProgress(TableColumn<Player, Float> resultProgress) {
        this.resultProgress = resultProgress;
    }


}
