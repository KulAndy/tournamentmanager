package com.example.tournamentmanager.helper;

import com.example.tournamentmanager.helper.tables.*;
import com.example.tournamentmanager.model.*;
import javafx.scene.control.*;

public class TablesHelper {
    private PolandTableHelper polandTablesHelper;
    private FideTableHelper fideTableHelper;
    private ResultTableHelper resultTableHelper;
    private FilterListHelper filterListHelper;
    private FilterCreatorHelper filterCreatorHelper;

    public TablesHelper(Tournament tournament,
                        TableView<Player> rtgPolTable,
                        TableColumn<Player, Integer> rtPolId, TableColumn<Player, Title> rtPolTitle,
                        TableColumn<Player, String> rtPolName, TableColumn<Player, Integer> rtPolGames,
                        TableColumn<Player, Float> rtPolPoints, TableColumn<Player, Integer> rtPolAverage,
                        TableColumn<Player, Integer> rtPolPerformance, TableColumn<Player, String> rtPolNorm,
                        TableColumn<Player, String> rtPolRemarks,
                        TableView<Player> rtgFideTable, TableColumn<Player, String> rtgFideName,
                        TableColumn<Player, Integer> rtgFideId, TableColumn<Player, Title> rtgFideTitle,
                        TableColumn<Player, Federation> rtgFideFed, TableColumn<Player, Integer> rtgFideElo,
                        TableColumn<Player, Float> rtgFidePoints, TableColumn<Player, Integer> rtgFideGames,
                        TableColumn<Player, Float> rtgFideAverage, TableColumn<Player, Float> rtgFideChg,
                        TableColumn<Player, Title> rtgFideNorm,
                        CheckBox resultFiltered, ComboBox<ResultPredicate<Player>> resultFilter, TableView<Player> resultsTable,
                        TableColumn<Player, Integer> resultPlace, TableColumn<Player, Integer> resultStartNo,
                        TableColumn<Player, Title> resultTitle, TableColumn<Player, String> resultName,
                        TableColumn<Player, Integer> resultElo, TableColumn<Player, Integer> resultLocal,
                        TableColumn<Player, Federation> resultFed, TableColumn<Player, Number> resultPoints,
                        TableColumn<Player, Number> resultBuchCut, TableColumn<Player, Number> resultBuch,
                        TableColumn<Player, Number> resultBerger, TableColumn<Player, Number> resultProgress,
                        TableView<ResultPredicate<Player>> filterListTable,
                        TableColumn<ResultPredicate<Player>, String> filterListName, TableColumn<ResultPredicate<Player>, Player.Sex> filterListSex,
                        TableColumn<ResultPredicate<Player>, String> filterListYear, TableColumn<ResultPredicate<Player>, String> filterListTitle,
                        TableColumn<ResultPredicate<Player>, String> filterListLocal, TableColumn<ResultPredicate<Player>, String> filterListFideRtg,
                        TableColumn<ResultPredicate<Player>, String> filterListFed, TableColumn<ResultPredicate<Player>, String> filterListState,
                        TableColumn<ResultPredicate<Player>, Void> filterListDelete,
                        TextField filterNameField, Button filterCreate, ComboBox<Player.Sex> filterSexSelect,
                        ComboBox<ResultPredicate.CompareOperator> filterYearOperator, TextField filterYearField,
                        ComboBox<ResultPredicate.CompareOperator> filterTitleOperator, ComboBox<Title> filterTitleSelect,
                        ComboBox<ResultPredicate.CompareOperator> filterLocalRtgOperator, TextField filterLocalRtg,
                        ComboBox<ResultPredicate.CompareOperator> filterFIDERtgOperator, TextField filterFIDERtg,
                        ComboBox<ResultPredicate.CompareOperator> filterFedOperator, ComboBox<Federation> filterFedSelect,
                        ComboBox<ResultPredicate.CompareOperator> filterStateOperator, ComboBox<String> filterStateSelect
    ) {
        setPolandTablesHelper(new PolandTableHelper(
                tournament, rtgPolTable, rtPolId, rtPolTitle, rtPolName, rtPolGames, rtPolPoints, rtPolAverage, rtPolPerformance, rtPolNorm, rtPolRemarks
        ));

        setFideTableHelper(new FideTableHelper(
                tournament, rtgFideTable, rtgFideName, rtgFideId, rtgFideTitle, rtgFideFed, rtgFideElo, rtgFidePoints, rtgFideGames, rtgFideAverage, rtgFideChg, rtgFideNorm
        ));

        setResultTableHelper(new ResultTableHelper(
                tournament,
                resultFiltered, resultFilter, resultsTable,
                resultPlace, resultStartNo, resultTitle, resultName,
                resultElo, resultLocal, resultFed, resultPoints,
                resultBuchCut, resultBuch, resultBerger, resultProgress
        ));

        setFilterListHelper(
                new FilterListHelper(
                        tournament, filterListTable,
                        filterListName, filterListSex,
                        filterListYear, filterListTitle,
                        filterListLocal, filterListFideRtg,
                        filterListFed, filterListState,
                        filterListDelete
                )
        );

        setFilterCreatorHelper(
                new FilterCreatorHelper(
                        tournament,
                        filterNameField, filterCreate, filterSexSelect,
                        filterYearOperator, filterYearField,
                        filterTitleOperator, filterTitleSelect,
                        filterLocalRtgOperator, filterLocalRtg,
                        filterFIDERtgOperator, filterFIDERtg,
                        filterFedOperator, filterFedSelect,
                        filterStateOperator, filterStateSelect
                )
        );
    }

    public PolandTableHelper getPolandTablesHelper() {
        return polandTablesHelper;
    }

    public void setPolandTablesHelper(PolandTableHelper polandTablesHelper) {
        this.polandTablesHelper = polandTablesHelper;
    }

    public FideTableHelper getFideTableHelper() {
        return fideTableHelper;
    }

    public void setFideTableHelper(FideTableHelper fideTableHelper) {
        this.fideTableHelper = fideTableHelper;
    }

    public ResultTableHelper getResultTableHelper() {
        return resultTableHelper;
    }

    public void setResultTableHelper(ResultTableHelper resultTableHelper) {
        this.resultTableHelper = resultTableHelper;
    }

    public FilterListHelper getFilterListHelper() {
        return filterListHelper;
    }

    public void setFilterListHelper(FilterListHelper filterListHelper) {
        this.filterListHelper = filterListHelper;
    }

    public FilterCreatorHelper getFilterCreatorHelper() {
        return filterCreatorHelper;
    }

    public void setFilterCreatorHelper(FilterCreatorHelper filterCreatorHelper) {
        this.filterCreatorHelper = filterCreatorHelper;
    }

}
