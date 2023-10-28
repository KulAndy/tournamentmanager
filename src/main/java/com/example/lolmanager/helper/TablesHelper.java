package com.example.lolmanager.helper;

import com.example.lolmanager.helper.tables.FideTableHelper;
import com.example.lolmanager.helper.tables.PolandTableHelper;
import com.example.lolmanager.helper.tables.ResultTableHelper;
import com.example.lolmanager.model.Federation;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.scene.control.*;

public class TablesHelper {
    private PolandTableHelper polandTablesHelper;
    private FideTableHelper fideTableHelper;
    private ResultTableHelper resultTableHelper;

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
                        TableColumn<Player, Integer> rtgFideAverage, TableColumn<Player, Float> rtgFideChg,
                        TableColumn<Player, Title> rtgFideNorm,
                        CheckBox resultFiltered, ComboBox resultFilter, TableView<Player> resultsTable,
                        TableColumn<Player, Integer> resultPlace, TableColumn<Player, Integer> resultStartNo,
                        TableColumn<Player, Title> resultTitle, TableColumn<Player, String> resultName,
                        TableColumn<Player, Integer> resultElo, TableColumn<Player, Integer> resultLocal,
                        TableColumn<Player, Federation> resultFed, TableColumn<Player, Float> resultPoints,
                        TableColumn<Player, Float> resultBuchCut, TableColumn<Player, Float> resultBuch,
                        TableColumn<Player, Float> resultBerger, TableColumn<Player, Float> resultProgress
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

}
