package com.example.lolmanager;

import com.example.lolmanager.comparator.StartListComparator;
import com.example.lolmanager.helper.*;
import com.example.lolmanager.model.*;
import com.example.lolmanager.operation.ExcelOperation;
import com.example.lolmanager.operation.FIDEOperation;
import com.example.lolmanager.operation.FileOperation;
import com.example.lolmanager.operation.TournamentOperation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.example.lolmanager.helper.GeneralHelper.*;
import static com.example.lolmanager.operation.TournamentOperation.*;

public class MainController implements Initializable {
    private final ObservableList<File> files = FXCollections.observableArrayList();
    @FXML
    ScrollPane allRoundsScroll;
    private String programName = "";
    private String programExtension = "*";
    private Tournament tournament;
    private File file;
    private boolean saving = false;
    private FileOperation fileOperation;
    private ShortcutsHelper shortcutsHelper;
    @FXML
    private Tab playersTab;
    @FXML
    private Tab startListTab;
    @FXML
    private Tab tablesTab;
    @FXML
    private Tab resultsTab;
    @FXML
    private ComboBox<File> tournamentSelect;
    @FXML
    private MenuItem newMenu;
    @FXML
    private MenuItem openMenu;
    @FXML
    private MenuItem closeMenu;
    @FXML
    private MenuItem saveMenu;
    @FXML
    private MenuItem saveAsMenu;
    @FXML
    private MenuItem exportPgnMenu;
    @FXML
    private MenuItem importTrf;
    @FXML
    private MenuItem importSwsx;
    @FXML
    private MenuItem importPgn;
    @FXML
    private CheckMenuItem autosaveMenu;
    @FXML
    private MenuItem quitMenu;
    @FXML
    private MenuItem fideReg;
    @FXML
    private MenuItem downloadFideMenu;
    @FXML
    private MenuItem trfRaport;

    @FXML
    private Button saveButton;
    @FXML
    private Button openButton;
    @FXML
    private Button printButton;
    @FXML
    private Button downloadFideButton;
    @FXML
    private Button downloadPolButton;
    @FXML
    private Button randomTournament;

    @FXML
    private Tab roundsTab;
    @FXML
    private Tab enterResultsTab;
    private HomeTabHelper homeTabHelper;
    @FXML
    private TextField tourName;
    @FXML
    private DatePicker tourStartDate;
    @FXML
    private DatePicker tourEndDate;
    @FXML
    private TextField tourPlace;
    @FXML
    private TextField tourGameTime;
    @FXML
    private TextField tourIncrement;
    @FXML
    private TextField tourControlMove;
    @FXML
    private TextField tourControlAddition;
    @FXML
    private ComboBox<Tournament.Type> tourType;
    @FXML
    private CheckBox tourRtPZSzach;
    @FXML
    private CheckBox tourRtFIDE;
    @FXML
    private TextField tourNoRounds;
    @FXML
    private ComboBox<Tournament.TournamentSystem> tourSystem;
    @FXML
    private TextField tourArbiter;
    @FXML
    private TextField tourOrganizer;
    @FXML
    private TextField tourEmail;
    @FXML
    private CheckBox tourFIDEMode;
    @FXML
    private GridPane tiebreakPane;
    @FXML
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB1;
    @FXML
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB2;
    @FXML
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB3;
    @FXML
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB4;
    @FXML
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB5;
    @FXML
    private GridPane pointsPane;
    @FXML
    private TextField pointsWin;
    @FXML
    private TextField pointsDraw;
    @FXML
    private TextField pointsLose;
    @FXML
    private TextField pointsForfeitWin;
    @FXML
    private TextField pointsForfeitLose;
    @FXML
    private TextField pointsBye;
    @FXML
    private TextField pointsHalfBye;
    @FXML
    private TextField minInitGames;
    @FXML
    private TextField ratingFloor;
    @FXML
    private CheckBox PZSzach43Cb;
    @FXML
    private CheckBox PZSzach44Cb;
    @FXML
    private CheckBox PZSzach46Cb;
    @FXML
    private CheckBox PZSzach47Cb;
    @FXML
    private ComboBox<Title> maxTitle;
    @FXML
    private CheckBox twoOtherFeds;
    @FXML
    private TextField minTitleGames;
    @FXML
    private TableView<Schedule.ScheduleElement> scheduleTable;
    @FXML
    private TableColumn<Schedule.ScheduleElement, String> scheduleName;
    @FXML
    private TableColumn<Schedule.ScheduleElement, Void> scheduleDate;
    private PlayersHelper playersHelper;

    @FXML
    private Button correctFide;
    @FXML
    private Button correctPl;
    @FXML
    private TableView<Player> playersListTable;
    @FXML
    private TableColumn<Player, Integer> startNoCol;
    @FXML
    private TableColumn<Player, Title> titleCol;
    @FXML
    private TableColumn<Player, String> nameCol;
    @FXML
    private TableColumn<Player, Federation> fedCol;
    @FXML
    private TableColumn<Player, Integer> fideCol;
    @FXML
    private TableColumn<Player, Integer> localCol;
    @FXML
    private TableColumn<Player, String> clubCol;
    @FXML
    private TableColumn<Player, Integer> localIdCol;
    @FXML
    private TableColumn<Player, Integer> fideIdCol;
    @FXML
    private TableColumn<Player, String> remarksCol;
    @FXML
    private TableColumn<Player, Void> deleteCol;
    @FXML
    private ComboBox<StartListComparator.SortCriteria> criteria1;
    @FXML
    private ComboBox<StartListComparator.SortCriteria> criteria2;
    @FXML
    private ComboBox<StartListComparator.SortCriteria> criteria3;
    @FXML
    private ComboBox<StartListComparator.SortCriteria> criteria4;
    @FXML
    private ComboBox<StartListComparator.SortCriteria> criteria5;
    @FXML
    private Button applySortButton;

    @FXML
    private ComboBox<Player> playerSelect;
    @FXML
    private ComboBox<Federation> fedSelect;
    @FXML
    private ComboBox<String> stateSelect;
    @FXML
    private TextField playerNameField;
    @FXML
    private ComboBox<Title> playerTitleSelect;
    @FXML
    private TextField localRtgField;
    @FXML
    private TextField FIDERtgField;
    @FXML
    private TextField clubField;
    @FXML
    private TextField dayOfBirth;
    @FXML
    private TextField monthOfBirth;
    @FXML
    private TextField yearOfBirth;
    @FXML
    private ComboBox<Player.Sex> sexSelect;
    @FXML
    private TextField mailField;
    @FXML
    private ComboBox<Short> phonePrefixSelect;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField localIDField;
    @FXML
    private TextField FIDEIDField;
    @FXML
    private TextField remarksField;
    @FXML
    private Button addPlayerButton;
    @FXML
    private Button updatePlayerBth;
    @FXML
    private Button clearPlayerButton;
    @FXML
    private Button addClearPlayerButton;
    @FXML
    private Button insertFromList;
    @FXML
    private ListView<Player> newPlayerHint;

    @FXML
    private ComboBox<Player> withdrawPlayerSelect;
    @FXML
    private ComboBox<Withdraw.WithdrawType> withdrawTypeSelect;
    @FXML
    private TextField withdrawRound;
    @FXML
    private Button acceptWithdrawButton;
    @FXML
    private TableView<Withdraw> withdrawTable;
    @FXML
    private TableColumn<Withdraw, Integer> withdrawNoCol;
    @FXML
    private TableColumn<Withdraw, String> withdrawNameCol;
    @FXML
    private TableColumn<Withdraw, Withdraw.WithdrawType> withdrawTypeCol;
    @FXML
    private TableColumn<Withdraw, Byte> withdrawRoundCol;
    @FXML
    private TableColumn<Withdraw, Void> withdrawBackCol;

    @FXML
    private ComboBox<Player> playerCardSelect;
    @FXML
    private Label playerCardName;
    @FXML
    private Label playerCardTB1;
    @FXML
    private Label playerCardTB1Value;
    @FXML
    private Label playerCardTB2;
    @FXML
    private Label playerCardTB2Value;
    @FXML
    private Label playerCardTB3;
    @FXML
    private Label playerCardTB3Value;
    @FXML
    private Label playerCardTB4;
    @FXML
    private Label playerCardTB4Value;
    @FXML
    private Label playerCardTB5;
    @FXML
    private Label playerCardTB5Value;
    @FXML
    private Label playerCardElo;
    @FXML
    private Label playerCardEloValue;
    @FXML
    private Label playerCardPZSzach;
    @FXML
    private Label playerCardPZSzachValue;
    @FXML
    private TableView<Game> playerCardGames;
    @FXML
    private TableColumn<Game, Integer> playerCardOppRound;
    @FXML
    private TableColumn<Game, Player.Color> playerCardOppColor;
    @FXML
    private TableColumn<Game, String> playerCardOppResult;
    @FXML
    private TableColumn<Game, Title> playerCardOppTitle;
    @FXML
    private TableColumn<Game, String> playerCardOppName;
    @FXML
    private TableColumn<Game, Integer> playerCardOppRtg;
    @FXML
    private GridPane cardGrid;

    private RoundsHelper roundsHelper;
    @FXML
    private ComboBox<Integer> roundUpdateSelect;
    @FXML
    private TextField whiteSearch;
    @FXML
    private TextField blackSearch;
    @FXML
    private ListView<Player> whiteList;
    @FXML
    private ListView<Player> blackList;
    @FXML
    private CheckBox autoColorCheckbox;
    @FXML
    private Button pairRestButton;
    @FXML
    private Button pairButton;
    @FXML
    private Button whithdrawButton;
    @FXML
    private Button byePairButton;
    @FXML
    private Button halfByePairButton;
    @FXML
    private Button clearManualButton;
    @FXML
    private Button unpairButton;
    @FXML
    private Button swapColorPairButton;
    @FXML
    private Button applyManualButton;
    @FXML
    private ListView<Game> pairsList;
    @FXML
    private ComboBox<Integer> roundsViewSelect;
    @FXML
    private Button firstRound;
    @FXML
    private Button previousRound;
    @FXML
    private Button nextRound;
    @FXML
    private Button lastRound;
    @FXML
    private Button whiteWinResult;
    @FXML
    private Button drawResult;
    @FXML
    private Button blackWinResult;
    @FXML
    private Button whiteWinForfeitResult;
    @FXML
    private Button blackWinForfeitResult;
    @FXML
    private Button deleteRound;
    @FXML
    private Button enginePairButton;

    @FXML
    private TableView<Game> gamesView;
    @FXML
    private TableColumn<Game, Integer> leftBoardNo;
    @FXML
    private TableColumn<Game, Float> whitePoints;
    @FXML
    private TableColumn<Game, Integer> whiteRating;
    @FXML
    private TableColumn<Game, String> whitePlayer;
    @FXML
    private TableColumn<Game, Void> gameResult;
    @FXML
    private TableColumn<Game, String> blackPlayer;
    @FXML
    private TableColumn<Game, Integer> blackRating;
    @FXML
    private TableColumn<Game, Float> blackPoints;
    @FXML
    private TableColumn<Game, Integer> rightBoardNo;
    private TablesHelper tablesHelper;
    @FXML
    private TableView<Player> rtgPolTable;
    @FXML
    private TableColumn<Player, Integer> rtPolId;
    @FXML
    private TableColumn<Player, Title> rtPolTitle;
    @FXML
    private TableColumn<Player, String> rtPolName;
    @FXML
    private TableColumn<Player, Integer> rtPolGames;
    @FXML
    private TableColumn<Player, Float> rtPolPoints;
    @FXML
    private TableColumn<Player, Integer> rtPolAverage;
    @FXML
    private TableColumn<Player, Integer> rtPolPerformance;
    @FXML
    private TableColumn<Player, String> rtPolNorm;
    @FXML
    private TableColumn<Player, String> rtPolRemarks;

    @FXML
    private TableView<Player> rtgFideTable;
    @FXML
    private TableColumn<Player, String> rtgFideName;
    @FXML
    private TableColumn<Player, Integer> rtgFideId;
    @FXML
    private TableColumn<Player, Title> rtgFideTitle;
    @FXML
    private TableColumn<Player, Federation> rtgFideFed;
    @FXML
    private TableColumn<Player, Integer> rtgFideElo;
    @FXML
    private TableColumn<Player, Float> rtgFidePoints;
    @FXML
    private TableColumn<Player, Integer> rtgFideGames;
    @FXML
    private TableColumn<Player, Integer> rtgFideAverage;
    @FXML
    private TableColumn<Player, Float> rtgFideChg;
    @FXML
    private TableColumn<Player, Title> rtgFideNorm;


    @FXML
    private CheckBox resultFiltered;
    @FXML
    private ComboBox<ResultPredicate<Player>> resultFilter;
    @FXML
    private TableView<Player> resultsTable;
    @FXML
    private TableColumn<Player, Integer> resultPlace;
    @FXML
    private TableColumn<Player, Integer> resultStartNo;
    @FXML
    private TableColumn<Player, Title> resultTitle;
    @FXML
    private TableColumn<Player, String> resultName;
    @FXML
    private TableColumn<Player, Integer> resultElo;
    @FXML
    private TableColumn<Player, Integer> resultLocal;
    @FXML
    private TableColumn<Player, Federation> resultFed;
    @FXML
    private TableColumn<Player, Number> resultTb1;
    @FXML
    private TableColumn<Player, Number> resultTb2;
    @FXML
    private TableColumn<Player, Number> resultTb3;
    @FXML
    private TableColumn<Player, Number> resultTb4;
    @FXML
    private TableColumn<Player, Number> resultTb5;

    @FXML
    private TableView<ResultPredicate<Player>> filterListTable;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListName;
    @FXML
    private TableColumn<ResultPredicate<Player>, Player.Sex> filterListSex;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListYear;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListTitle;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListLocal;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListFideRtg;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListFed;
    @FXML
    private TableColumn<ResultPredicate<Player>, String> filterListState;
    @FXML
    private TableColumn<ResultPredicate<Player>, Void> filterListDelete;

    @FXML
    private TextField filterNameField;
    @FXML
    private Button filterCreate;
    @FXML
    private ComboBox<Player.Sex> filterSexSelect;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterYearOperator;
    @FXML
    private TextField filterYearField;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterTitleOperator;
    @FXML
    private ComboBox<Title> filterTitleSelect;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterLocalRtgOperator;
    @FXML
    private TextField filterLocalRtg;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterFIDERtgOperator;
    @FXML
    private TextField filterFIDERtg;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterFedOperator;
    @FXML
    private ComboBox<Federation> filterFedSelect;
    @FXML
    private ComboBox<ResultPredicate.CompareOperator> filterStateOperator;
    @FXML
    private ComboBox<String> filterStateSelect;


    public static void quit() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public Tournament getTournament() {
        return tournament;
    }

    @FXML
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        homeTabHelper = new HomeTabHelper(
                tournament, tourName, tourStartDate, tourEndDate, tourPlace, tourGameTime,
                tourIncrement, tourControlMove, tourControlAddition, tourType, tourRtPZSzach, tourRtFIDE,
                tourNoRounds, tourSystem, tourArbiter, tourOrganizer, tourEmail,
                tourFIDEMode, tiebreakPane, tourTB1, tourTB2, tourTB3, tourTB4, tourTB5,
                pointsPane, pointsWin, pointsDraw, pointsLose, pointsForfeitWin, pointsForfeitLose,
                pointsBye, pointsHalfBye,
                minInitGames, ratingFloor, PZSzach43Cb, PZSzach44Cb, PZSzach46Cb, PZSzach47Cb, maxTitle, twoOtherFeds, minTitleGames,
                scheduleTable, scheduleName, scheduleDate
        );
        playersHelper = new PlayersHelper(
                tournament,
                correctFide, correctPl,
                playersListTable,
                startNoCol, titleCol, nameCol, fedCol, fideCol,
                localCol, clubCol, localIdCol, fideIdCol, remarksCol, deleteCol,
                criteria1, criteria2, criteria3, criteria4, criteria5,
                applySortButton,
                playerSelect,
                fedSelect, stateSelect, playerNameField,
                playerTitleSelect, localRtgField, FIDERtgField,
                clubField, dayOfBirth, monthOfBirth, yearOfBirth,
                sexSelect, mailField, phonePrefixSelect,
                phoneNumber, localIDField, FIDEIDField, remarksField,
                addPlayerButton, updatePlayerBth, clearPlayerButton, addClearPlayerButton,
                insertFromList, newPlayerHint,
                playerCardSelect,
                playerCardName, playerCardTB1, playerCardTB1Value,
                playerCardTB2, playerCardTB2Value, playerCardTB3, playerCardTB3Value,
                playerCardTB4, playerCardTB4Value, playerCardTB5, playerCardTB5Value,
                playerCardElo, playerCardEloValue, playerCardPZSzach, playerCardPZSzachValue,
                playerCardGames, playerCardOppRound, playerCardOppColor, playerCardOppResult,
                playerCardOppTitle, playerCardOppName, playerCardOppRtg, cardGrid
        );

        roundsHelper = new RoundsHelper(
                tournament, roundUpdateSelect, whiteSearch, blackSearch, whiteList, blackList,
                autoColorCheckbox, pairRestButton, pairButton, whithdrawButton, byePairButton, halfByePairButton,
                clearManualButton, unpairButton, swapColorPairButton,
                applyManualButton, pairsList, roundsViewSelect, firstRound, previousRound, nextRound, lastRound, whiteWinResult, drawResult,
                blackWinResult, whiteWinForfeitResult, blackWinForfeitResult, gamesView,
                leftBoardNo, whitePoints, whiteRating, whitePlayer, gameResult, blackPlayer,
                blackRating, blackPoints, rightBoardNo, deleteRound, enginePairButton,
                allRoundsScroll,
                withdrawPlayerSelect, withdrawTypeSelect, withdrawRound, acceptWithdrawButton, withdrawTable,
                withdrawNoCol, withdrawNameCol, withdrawTypeCol, withdrawRoundCol, withdrawBackCol

        );

        setTablesHelper(new TablesHelper(
                tournament, rtgPolTable, rtPolId, rtPolTitle, rtPolName, rtPolGames, rtPolPoints, rtPolAverage, rtPolPerformance, rtPolNorm, rtPolRemarks,
                rtgFideTable, rtgFideName, rtgFideId, rtgFideTitle, rtgFideFed, rtgFideElo, rtgFidePoints, rtgFideGames, rtgFideAverage, rtgFideChg, rtgFideNorm,
                resultFiltered, resultFilter, resultsTable,
                resultPlace, resultStartNo, resultTitle, resultName,
                resultElo, resultLocal, resultFed, resultTb1,
                resultTb2, resultTb3, resultTb4, resultTb5,
                filterListTable,
                filterListName, filterListSex,
                filterListYear, filterListTitle,
                filterListLocal, filterListFideRtg,
                filterListFed, filterListState,
                filterListDelete,
                filterNameField, filterCreate, filterSexSelect,
                filterYearOperator, filterYearField,
                filterTitleOperator, filterTitleSelect,
                filterLocalRtgOperator, filterLocalRtg,
                filterFIDERtgOperator, filterFIDERtg,
                filterFedOperator, filterFedSelect,
                filterStateOperator, filterStateSelect
        ));

        tournamentSelect.setItems(files);

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(5), event -> {
            if (getAutosaveMenu().isSelected() && !isSaving()) {
                setSaving(true);
                try {
                    save(this);
                } catch (IOException e) {
                    error("Couldn't save tournament");
                }
                setSaving(false);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void init(Scene scene, String programName, String programExtension) {
        setProgramName(programName);
        setProgramExtension(programExtension);
        setFileOperation(new FileOperation());
        setupEvents();
        setShortcutsHelper(new ShortcutsHelper(scene, getFileOperation(), roundsHelper, this));

    }

    public void setupEvents() {
        getQuitMenu().setOnAction(e -> quit());
        getSaveAsMenu().setOnAction(e -> {
            try {
                saveAs(this);
            } catch (IOException ex) {
                error("Couldn't save tournament");
            }
        });
        getSaveMenu().setOnAction(e -> {
            try {
                save(this);
            } catch (IOException ex) {
                error("Couldn't save tournament");
            }
        });
        getSaveButton().setOnAction(e -> {
            try {
                save(this);
            } catch (IOException ex) {
                error("Couldn't save tournament");
            }
        });
        getOpenMenu().setOnAction(e -> open(this));
        getOpenButton().setOnAction(e -> open(this));
        getPrintButton().setOnAction(e -> shortcutsHelper.print());
        fideReg.setOnAction(e -> ExcelOperation.createApplication(tournament, getProgramName()));
        trfRaport.setOnAction(e -> FIDEOperation.selectTrfReport(getTournament()));
        exportPgnMenu.setOnAction(e ->
                GeneralHelper.threeOptionsDialog("Export mode", "tournament", "round")
                        .thenAccept(choice -> {
                            if (choice.equals("A")) {
                                try {
                                    TournamentOperation.exportPgn(getTournament());
                                    info("Exported games to pgn successfully");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    System.out.println(ex.getMessage());
                                    error("Error during exporting rounds");
                                }
                            } else if (choice.equals("B")) {
                                Integer currentRound = getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().getValue();
                                if (currentRound == null) {
                                    warning("Nothing exported\nNo round has been selected");
                                } else {
                                    try {
                                        TournamentOperation.exportRoundPgn(getTournament().getRound(currentRound - 1), getTournament());
                                        info("Export round %d successfully".formatted(currentRound));
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                        System.out.println(ex.getMessage());
                                        error("Error during export round %d".formatted(currentRound));
                                    }
                                }
                            }
                        }));
        downloadFideMenu.setOnAction(e -> {
            CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                    .exceptionally(ex -> null);
        });
        downloadFideButton.setOnAction(e -> {
            CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                    .exceptionally(ex -> null);
        });
        downloadPolButton.setOnAction(e -> {
            CompletableFuture.runAsync(FileOperation::downloadPolList)
                    .exceptionally(ex -> null);
        });
        importPgn.setOnAction(e -> TournamentOperation.importPgn(this));

        randomTournament.setOnAction(e -> {
            try {
                if (getTournament().getSystem() == Tournament.TournamentSystem.SWISS) {
                    TournamentOperation.loadTournament(JavafoWrapper.generateRandomTournament(), this);
                } else if (getTournament().getSystem() == Tournament.TournamentSystem.ROUND_ROBIN) {
                    TournamentOperation.loadTournament(RoundRobinEngine.generateRandomTournament(), this);
                }
            } catch (IOException | InterruptedException ex) {
                error("Couldn't generate random tournament");
            }
        });

        importTrf.setOnAction(e -> FIDEOperation.importTrfReport(this));

        importSwsx.setOnAction(e -> {
            File swsx = FileOperation.selectSwsx();
            if (swsx != null) {
                try {
                    SwsxTournament swsxTournament = new SwsxTournament(swsx);
                    Tournament tournament = new Tournament(swsxTournament);
                    TournamentOperation.loadTournament(tournament, this);
                    info("Imported successfully");
                } catch (Exception ex) {
                    error("An error eccured");
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            } else {
                warning("No file selected");
            }
        });

        closeMenu.setOnAction(e -> {
            files.remove(getFile());
        });
        tournamentSelect.valueProperty().addListener((ObservableValue<? extends File> observable, File oldValue, File newValue) -> {
            try {
                if (newValue != oldValue && newValue != getFile()) {
                    save(this);
                }
            } catch (IOException ex) {
                error("An error eccured");
                return;
            }
            if (newValue == null) {
                TournamentOperation.loadTournament(new Tournament(), this);
            } else {
                if (newValue != oldValue && newValue != getFile()) {
                    importJson(tournamentSelect.getValue(), this);
                }
            }
        });

        tourTB1.valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getResultsComparator().setCriteria1(newValue);
            String tiebreak = newValue.prettyText();
            playerCardTB1.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTiebreak(newValue).toString());
            }
            resultTb1.setText(tiebreak);
            tablesHelper.getResultTableHelper().refreshList();
        });
        tourTB2.valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getResultsComparator().setCriteria2(newValue);
            String tiebreak = newValue.prettyText();
            playerCardTB2.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTiebreak(newValue).toString());
            }
            resultTb2.setText(tiebreak);
            tablesHelper.getResultTableHelper().refreshList();
        });
        tourTB3.valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getResultsComparator().setCriteria3(newValue);
            String tiebreak = newValue.prettyText();
            playerCardTB3.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTiebreak(newValue).toString());
            }
            resultTb3.setText(tiebreak);
            tablesHelper.getResultTableHelper().refreshList();
        });
        tourTB4.valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getResultsComparator().setCriteria4(newValue);
            String tiebreak = newValue.prettyText();
            playerCardTB4.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTiebreak(newValue).toString());
            }
            resultTb4.setText(tiebreak);
            tablesHelper.getResultTableHelper().refreshList();
        });
        tourTB5.valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getResultsComparator().setCriteria5(newValue);
            String tiebreak = newValue.prettyText();
            playerCardTB5.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTiebreak(newValue).toString());
            }
            resultTb5.setText(tiebreak);
            tablesHelper.getResultTableHelper().refreshList();
        });
    }

    public ShortcutsHelper getShortcutsHelper() {
        return shortcutsHelper;
    }

    public void setShortcutsHelper(ShortcutsHelper shortcutsHelper) {
        this.shortcutsHelper = shortcutsHelper;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramExtension() {
        return programExtension;
    }

    public void setProgramExtension(String programExtension) {
        this.programExtension = programExtension;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        if (!files.contains(file)) {
            files.add(file);
            tournamentSelect.setValue(file);
        }
    }

    public FileOperation getFileOperation() {
        return fileOperation;
    }

    public void setFileOperation(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }

    public MenuItem getNewMenu() {
        return newMenu;
    }

    public MenuItem getOpenMenu() {
        return openMenu;
    }

    public MenuItem getCloseMenu() {
        return closeMenu;
    }

    public MenuItem getSaveMenu() {
        return saveMenu;
    }

    public MenuItem getSaveAsMenu() {
        return saveAsMenu;
    }

    public CheckMenuItem getAutosaveMenu() {
        return autosaveMenu;
    }

    public MenuItem getQuitMenu() {
        return quitMenu;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getOpenButton() {
        return openButton;
    }

    public Button getPrintButton() {
        return printButton;
    }

    public TextField getTourName() {
        return tourName;
    }

    public DatePicker getTourStartDate() {
        return tourStartDate;
    }

    public DatePicker getTourEndDate() {
        return tourEndDate;
    }

    public TextField getTourPlace() {
        return tourPlace;
    }

    public TextField getTourGameTime() {
        return tourGameTime;
    }

    public TextField getTourIncrement() {
        return tourIncrement;
    }

    public TextField getTourControlMove() {
        return tourControlMove;
    }

    public TextField getTourControlAddition() {
        return tourControlAddition;
    }

    public ComboBox<Tournament.Type> getTourType() {
        return tourType;
    }

    public CheckBox getTourRtPZSzach() {
        return tourRtPZSzach;
    }

    public CheckBox getTourRtFIDE() {
        return tourRtFIDE;
    }

    public TextField getTourNoRounds() {
        return tourNoRounds;
    }

    public ComboBox<Tournament.TournamentSystem> getTourSystem() {
        return tourSystem;
    }

    public TextField getTourArbiter() {
        return tourArbiter;
    }

    public TextField getTourOrganizer() {
        return tourOrganizer;
    }

    public TextField getTourEmail() {
        return tourEmail;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB1() {
        return tourTB1;
    }

    public void setTourTB1(ComboBox<Tournament.Tiebreak.TbMethod> tourTB1) {
        this.tourTB1 = tourTB1;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB2() {
        return tourTB2;
    }

    public void setTourTB2(ComboBox<Tournament.Tiebreak.TbMethod> tourTB2) {
        this.tourTB2 = tourTB2;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB3() {
        return tourTB3;
    }

    public void setTourTB3(ComboBox<Tournament.Tiebreak.TbMethod> tourTB3) {
        this.tourTB3 = tourTB3;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB4() {
        return tourTB4;
    }

    public void setTourTB4(ComboBox<Tournament.Tiebreak.TbMethod> tourTB4) {
        this.tourTB4 = tourTB4;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB5() {
        return tourTB5;
    }

    public void setTourTB5(ComboBox<Tournament.Tiebreak.TbMethod> tourTB5) {
        this.tourTB5 = tourTB5;
    }

    public TextField getPointsWin() {
        return pointsWin;
    }

    public void setPointsWin(TextField pointsWin) {
        this.pointsWin = pointsWin;
    }

    public TextField getPointsDraw() {
        return pointsDraw;
    }

    public void setPointsDraw(TextField pointsDraw) {
        this.pointsDraw = pointsDraw;
    }

    public TextField getPointsLose() {
        return pointsLose;
    }

    public void setPointsLose(TextField pointsLose) {
        this.pointsLose = pointsLose;
    }

    public TextField getPointsForfeitWin() {
        return pointsForfeitWin;
    }

    public void setPointsForfeitWin(TextField pointsForfeitWin) {
        this.pointsForfeitWin = pointsForfeitWin;
    }

    public TextField getPointsForfeitLose() {
        return pointsForfeitLose;
    }

    public void setPointsForfeitLose(TextField pointsForfeitLose) {
        this.pointsForfeitLose = pointsForfeitLose;
    }

    public TextField getPointsBye() {
        return pointsBye;
    }

    public void setPointsBye(TextField pointsBye) {
        this.pointsBye = pointsBye;
    }

    public TextField getPointsHalfBye() {
        return pointsHalfBye;
    }

    public void setPointsHalfBye(TextField pointsHalfBye) {
        this.pointsHalfBye = pointsHalfBye;
    }

    public TextField getMinInitGames() {
        return minInitGames;
    }

    public void setMinInitGames(TextField minInitGames) {
        this.minInitGames = minInitGames;
    }

    public TextField getRatingFloor() {
        return ratingFloor;
    }

    public void setRatingFloor(TextField ratingFloor) {
        this.ratingFloor = ratingFloor;
    }

    public CheckBox getPZSzach43Cb() {
        return PZSzach43Cb;
    }

    public void setPZSzach43Cb(CheckBox PZSzach43Cb) {
        this.PZSzach43Cb = PZSzach43Cb;
    }

    public CheckBox getPZSzach44Cb() {
        return PZSzach44Cb;
    }

    public void setPZSzach44Cb(CheckBox PZSzach44Cb) {
        this.PZSzach44Cb = PZSzach44Cb;
    }

    public CheckBox getPZSzach46Cb() {
        return PZSzach46Cb;
    }

    public void setPZSzach46Cb(CheckBox PZSzach46Cb) {
        this.PZSzach46Cb = PZSzach46Cb;
    }

    public CheckBox getPZSzach47Cb() {
        return PZSzach47Cb;
    }

    public void setPZSzach47Cb(CheckBox PZSzach47Cb) {
        this.PZSzach47Cb = PZSzach47Cb;
    }

    public ComboBox<Title> getMaxTitle() {
        return maxTitle;
    }

    public void setMaxTitle(ComboBox<Title> maxTitle) {
        this.maxTitle = maxTitle;
    }

    public CheckBox getTwoOtherFeds() {
        return twoOtherFeds;
    }

    public void setTwoOtherFeds(CheckBox twoOtherFeds) {
        this.twoOtherFeds = twoOtherFeds;
    }

    public TextField getMinTitleGames() {
        return minTitleGames;
    }

    public void setMinTitleGames(TextField minTitleGames) {
        this.minTitleGames = minTitleGames;
    }

    public PlayersHelper getPlayersHelper() {
        return playersHelper;
    }

    public void setPlayersHelper(PlayersHelper playersHelper) {
        this.playersHelper = playersHelper;
    }

    public HomeTabHelper getHomeTabHelper() {
        return homeTabHelper;
    }

    public void setHomeTabHelper(HomeTabHelper homeTabHelper) {
        this.homeTabHelper = homeTabHelper;
    }

    public TableColumn getDeleteCol() {
        return deleteCol;
    }

    public void setDeleteCol(TableColumn deleteCol) {
        this.deleteCol = deleteCol;
    }

    public RoundsHelper getRoundsHelper() {
        return roundsHelper;
    }

    public void setRoundsHelper(RoundsHelper roundsHelper) {
        this.roundsHelper = roundsHelper;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }

    public Tab getPlayersTab() {
        return playersTab;
    }

    public Tab getStartListTab() {
        return startListTab;
    }

    public Tab getTablesTab() {
        return tablesTab;
    }

    public Tab getResultsTab() {
        return resultsTab;
    }

    public Tab getRoundsTab() {
        return roundsTab;
    }

    public Tab getEnterResultsTab() {
        return enterResultsTab;
    }

    public TablesHelper getTablesHelper() {
        return tablesHelper;
    }

    public void setTablesHelper(TablesHelper tablesHelper) {
        this.tablesHelper = tablesHelper;
    }
}

