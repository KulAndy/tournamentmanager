package com.example.lolmanager.helper;

import com.example.lolmanager.helper.tables.FideTableHelper;
import com.example.lolmanager.helper.tables.PolandTableHelper;
import com.example.lolmanager.helper.tables.ResultTableHelper;
import com.example.lolmanager.model.Federation;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TablesHelper {
    private final PolandTableHelper polandTablesHelper;
    private final FideTableHelper fideTableHelper;
    private final ResultTableHelper resultTableHelper;

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
        this.polandTablesHelper = new PolandTableHelper(
                tournament, rtgPolTable, rtPolId, rtPolTitle, rtPolName, rtPolGames, rtPolPoints, rtPolAverage, rtPolPerformance, rtPolNorm, rtPolRemarks
        );

        this.fideTableHelper = new FideTableHelper(
                tournament, rtgFideTable, rtgFideName, rtgFideId, rtgFideTitle, rtgFideFed, rtgFideElo, rtgFidePoints, rtgFideGames, rtgFideAverage, rtgFideChg, rtgFideNorm
        );

        this.resultTableHelper = new ResultTableHelper(
                tournament,
                resultFiltered, resultFilter, resultsTable,
                resultPlace, resultStartNo, resultTitle, resultName,
                resultElo, resultLocal, resultFed, resultPoints,
                resultBuchCut, resultBuch, resultBerger, resultProgress
        );
    }
}
