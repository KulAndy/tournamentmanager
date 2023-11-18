package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ResultTableHelper {
    private Tournament tournament;
    private CheckBox resultFiltered;
    private ComboBox<ResultPredicate<Player>> resultFilter;
    private TableView<Player> resultsTable;
    private TableColumn<Player, Integer> resultPlace;
    private TableColumn<Player, Integer> resultStartNo;
    private TableColumn<Player, Title> resultTitle;
    private TableColumn<Player, String> resultName;
    private TableColumn<Player, Integer> resultElo;
    private TableColumn<Player, Integer> resultLocal;
    private TableColumn<Player, Federation> resultFed;
    private TableColumn<Player, Number> resultTb1;
    private TableColumn<Player, Number> resultTb2;
    private TableColumn<Player, Number> resultTb3;
    private TableColumn<Player, Number> resultTb4;
    private TableColumn<Player, Number> resultTb5;
    private SortedList<Player> sortedPlayers;

    public ResultTableHelper(
            Tournament tournament,
            CheckBox resultFiltered, ComboBox<ResultPredicate<Player>> resultFilter, TableView<Player> resultsTable,
            TableColumn<Player, Integer> resultPlace, TableColumn<Player, Integer> resultStartNo,
            TableColumn<Player, Title> resultTitle, TableColumn<Player, String> resultName,
            TableColumn<Player, Integer> resultElo, TableColumn<Player, Integer> resultLocal,
            TableColumn<Player, Federation> resultFed, TableColumn<Player, Number> resultTb1,
            TableColumn<Player, Number> resultTb2, TableColumn<Player, Number> resultTb3,
            TableColumn<Player, Number> resultTb4, TableColumn<Player, Number> resultTb5
    ) {
        setTournament(tournament);
        setResultFiltered(resultFiltered);
        setResultFilter(resultFilter);
        setResultsTable(resultsTable);
        setResultPlace(resultPlace);
        setResultStartNo(resultStartNo);
        setResultTitle(resultTitle);
        setResultName(resultName);
        setResultElo(resultElo);
        setResultLocal(resultLocal);
        setResultFed(resultFed);
        setResultTb1(resultTb1);
        setResultTb2(resultTb2);
        setResultTb3(resultTb3);
        setResultTb4(resultTb4);
        setResultTb5(resultTb5);

        getResultFiltered().selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue != null) {
                ResultPredicate<Player> predicate = getResultFilter().getValue();
                SortedList<Player> sortedList;
                if (newValue && predicate != null) {
                    FilteredList<Player> filteredList = new FilteredList<>(getTournament().getPlayersObs(), predicate);
                    sortedList = new SortedList<>(filteredList, getTournament().getResultsComparator());
                    setSortedPlayers(sortedList);
                } else {
                    setUnfilteredList();
                }
            }
        });

        getResultFilter().setItems(getTournament().getPredicatesObs());
        getResultFilter().valueProperty().addListener((ObservableValue<? extends ResultPredicate<Player>> observable, ResultPredicate<Player> oldValue, ResultPredicate<Player> newValue) -> {
            if (newValue != null) {
                SortedList<Player> sortedList;
                if (getResultFiltered().isSelected()) {
                    FilteredList<Player> filteredList = new FilteredList<>(getTournament().getPlayersObs(), newValue);
                    sortedList = new SortedList<>(filteredList, getTournament().getResultsComparator());
                    setSortedPlayers(sortedList);
                } else {
                    setUnfilteredList();
                }
            }
        });

        getResultFilter().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ResultPredicate<Player> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFilterName());
                }
            }
        });
        getResultFilter().setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ResultPredicate<Player> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFilterName());
                }
            }
        });

        getResultPlace().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getResultsTable().getItems().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });
        getResultStartNo().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            int rowIndex = getTournament().getPlayersObs().indexOf(player) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });

        getResultTitle().setCellValueFactory(new PropertyValueFactory<>("title"));
        getResultName().setCellValueFactory(new PropertyValueFactory<>("name"));
        getResultElo().setCellValueFactory(new PropertyValueFactory<>("fideRating"));
        getResultLocal().setCellValueFactory(new PropertyValueFactory<>("localRating"));
        getResultFed().setCellValueFactory(new PropertyValueFactory<>("federation"));

        setUnfilteredList();

        getResultsTable().setItems(getSortedPlayers());

        getTournament().getPlayersObs().addListener((ListChangeListener<? super Player>) change -> {
            setUnfilteredList();
        });

        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            setUnfilteredList();
        });

    }

    public void setUnfilteredList() {
        SortedList<Player> sortedList = new SortedList<>(getTournament().getPlayersObs(), getTournament().getResultsComparator());
        setSortedPlayers(sortedList);
    }

    public void refreshList() {
        setSortedPlayers(getSortedPlayers());
    }

    public SortedList<Player> getSortedPlayers() {
        return sortedPlayers;
    }

    public void setSortedPlayers(SortedList<Player> sortedPlayers) {
        this.sortedPlayers = sortedPlayers;
        getResultsTable().setItems(sortedPlayers);
        getResultTb1().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            return new SimpleFloatProperty((Float) player.getTiebreak(getTournament().getResultsComparator().getCriteria1()));
        });
        getResultTb2().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            return new SimpleFloatProperty((Float) player.getTiebreak(getTournament().getResultsComparator().getCriteria2()));
        });
        getResultTb3().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            return new SimpleFloatProperty((Float) player.getTiebreak(getTournament().getResultsComparator().getCriteria3()));
        });
        getResultTb4().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            return new SimpleFloatProperty((Float) player.getTiebreak(getTournament().getResultsComparator().getCriteria4()));
        });
        getResultTb5().setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            return new SimpleFloatProperty((Float) player.getTiebreak(getTournament().getResultsComparator().getCriteria5()));
        });
        getResultsTable().refresh();
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

    public ComboBox<ResultPredicate<Player>> getResultFilter() {
        return resultFilter;
    }

    public void setResultFilter(ComboBox<ResultPredicate<Player>> resultFilter) {
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

    public TableColumn<Player, Number> getResultTb1() {
        return resultTb1;
    }

    public void setResultTb1(TableColumn<Player, Number> resultTb1) {
        this.resultTb1 = resultTb1;
    }

    public TableColumn<Player, Number> getResultTb2() {
        return resultTb2;
    }

    public void setResultTb2(TableColumn<Player, Number> resultTb2) {
        this.resultTb2 = resultTb2;
    }

    public TableColumn<Player, Number> getResultTb3() {
        return resultTb3;
    }

    public void setResultTb3(TableColumn<Player, Number> resultTb3) {
        this.resultTb3 = resultTb3;
    }

    public TableColumn<Player, Number> getResultTb4() {
        return resultTb4;
    }

    public void setResultTb4(TableColumn<Player, Number> resultTb4) {
        this.resultTb4 = resultTb4;
    }

    public TableColumn<Player, Number> getResultTb5() {
        return resultTb5;
    }

    public void setResultTb5(TableColumn<Player, Number> resultTb5) {
        this.resultTb5 = resultTb5;
    }


}
