package com.example.tournamentmanager;

import com.example.tournamentmanager.comparator.ResultsComparator;
import com.example.tournamentmanager.comparator.StartListComparator;
import com.example.tournamentmanager.helper.*;
import com.example.tournamentmanager.model.*;
import com.example.tournamentmanager.operation.ExcelOperation;
import com.example.tournamentmanager.operation.FIDEOperation;
import com.example.tournamentmanager.operation.FileOperation;
import com.example.tournamentmanager.operation.TournamentOperation;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.example.tournamentmanager.helper.DialogHelper.error;
import static com.example.tournamentmanager.operation.FileOperation.updateTomlInZip;
import static com.example.tournamentmanager.operation.TournamentOperation.*;

public class MainController implements Initializable {
    private final ObservableList<File> files = FXCollections.observableArrayList();
    @FXML
    ScrollPane allRoundsScroll;
    private String programName = "";
    private String programExtension = "*";
    private Tournament tournament;
    private File file;
    private boolean saving = false;
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
    private MenuItem remoteSwsx;
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
    private MenuItem upload;
    @FXML
    private MenuItem userTournaments;
    @FXML
    private MenuItem login;
    @FXML
    private MenuItem register;
    @FXML
    private MenuItem checkUpdates;
    @FXML
    private MenuItem about;

    @FXML
    private Button newButton;
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
    private Button validateTournament;

    @FXML
    private Tab roundsTab;
    @FXML
    private Tab enterResultsTab;
    @FXML
    private Tab allRoundsTab;
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
    private RadioButton whiteColor;
    @FXML
    private RadioButton blackColor;
    @FXML
    private RadioButton autoColor;

    @FXML
    private CheckBox tourFIDEMode;
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
    private CheckBox PZSzach45Cb;
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
    private Label playerCardEloValue;
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
    private Button roundValidation;
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
    private TableColumn<Player, Float> rtgFideAverage;
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
        setHomeTabHelper(new HomeTabHelper(
                tournament, tourName, tourStartDate, tourEndDate, tourPlace, tourGameTime,
                tourIncrement, tourControlMove, tourControlAddition, tourType, tourRtPZSzach, tourRtFIDE,
                tourNoRounds, tourSystem, tourArbiter, tourOrganizer, tourEmail,
                whiteColor, blackColor, autoColor,
                tourFIDEMode, tourTB1, tourTB2, tourTB3, tourTB4, tourTB5,
                pointsPane, pointsWin, pointsDraw, pointsLose, pointsForfeitWin, pointsForfeitLose,
                pointsBye, pointsHalfBye,
                minInitGames, ratingFloor, PZSzach43Cb, PZSzach44Cb, PZSzach45Cb, PZSzach46Cb, PZSzach47Cb, maxTitle, twoOtherFeds, minTitleGames,
                scheduleTable, scheduleName, scheduleDate
        ));
        tourRtPZSzach.selectedProperty().addListener(e -> rtgPolTable.refresh());
        tourRtFIDE.selectedProperty().addListener(e -> rtgFideTable.refresh());

        setPlayersHelper(new PlayersHelper(
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
                playerCardEloValue, playerCardPZSzachValue,
                playerCardGames, playerCardOppRound, playerCardOppColor, playerCardOppResult,
                playerCardOppTitle, playerCardOppName, playerCardOppRtg
        ));

        setRoundsHelper(new RoundsHelper(
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

        ));

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
                save(this);
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
        setupEvents();
        setShortcutsHelper(new ShortcutsHelper(scene, roundsHelper, this));

    }

    public void setupEvents() {
        getNewMenu().setOnAction(e -> {
            save(this);
            setTournament(new Tournament());
            loadTournament(new Tournament(), this);
            tournamentSelect.setValue(null);
            setFile(null);
        });
        getQuitMenu().setOnAction(e -> quit());
        getSaveAsMenu().setOnAction(e -> saveAs(this));
        getSaveMenu().setOnAction(e -> save(this));
        getNewButton().setOnAction(e -> {
            save(this);
            setTournament(new Tournament());
            loadTournament(new Tournament(), this);
            tournamentSelect.setValue(null);
            setFile(null);
        });
        getSaveButton().setOnAction(e -> save(this));
        getOpenMenu().setOnAction(e -> open(this));
        getOpenButton().setOnAction(e -> open(this));
        getPrintButton().setOnAction(e -> shortcutsHelper.print());
        fideReg.setOnAction(e -> ExcelOperation.createApplication(tournament, getProgramName()));
        trfRaport.setOnAction(e -> FIDEOperation.selectTrfReport(getTournament()));
        about.setOnAction(e -> {
            Task<Void> openPdfTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Desktop.getDesktop().open(new File(Objects.requireNonNull(getClass().getResource("man.pdf")).toURI()));
                    } catch (IOException | URISyntaxException ex) {
                        error("Could not open manual");
                        try {
                            URI uri = new URI("https://github.com/KulAndy/tournamentmanager");
                            Desktop.getDesktop().browse(uri);
                        } catch (IOException | URISyntaxException ex2) {
                            error("Could not open project page");
                        }
                    }
                    return null;
                }
            };

            Thread thread = new Thread(openPdfTask);
            thread.setDaemon(true);
            thread.start();
        });
        upload.setOnAction(e -> CompletableFuture.runAsync(() -> {
                    Toml toml;
                    String serverUrl;

                    try {
                        toml = new Toml().read(new File("settings.toml"));
                        serverUrl = toml.getTable("remote").getString("api");
                    } catch (Exception ex) {
                        try {
                            URI uri = new URI("https://raw.githubusercontent.com/KulAndy/tournamentmanager/master/settings.toml");
                            URL defaultTomlURL = uri.toURL();
                            byte[] defaultTomlBytes = Files.readAllBytes(Paths.get(defaultTomlURL.toURI()));
                            String defaultTomlContent = new String(defaultTomlBytes);

                            toml = new Toml().read(defaultTomlContent);
                            serverUrl = toml.getTable("remote").getString("api");
                        } catch (IOException | URISyntaxException ex1) {
                            error("Couldn't read server location");
                            return;
                        }
                    }

                    if (file == null) {
                        error("Can not upload unsaved tournament");
                    } else {
                        if (file.exists()) {

                            HttpClient httpClient = HttpClients.createDefault();
                            HttpPost httpPost = new HttpPost(serverUrl + toml.getTable("remote").getString("upload"));
                            ArrayList<String> lines;
                            try {
                                lines = (ArrayList<String>) Files.readAllLines(Paths.get("auth.txt"), StandardCharsets.UTF_8);
                            } catch (IOException ex) {
                                error("You aren't log in or session expired");
                                return;
                            }


                            if (!lines.isEmpty()) {
                                String token = lines.get(0);

                                httpPost.setHeader("token", token);
                            } else {
                                error("Corrupted auth file");
                                return;
                            }

                            try {
                                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                                builder.addPart("zipFile", new FileBody(file, ContentType.DEFAULT_BINARY, file.getName()));

                                HttpEntity multipart = builder.build();
                                httpPost.setEntity(multipart);

                                HttpResponse response = httpClient.execute(httpPost);
                                int statusCode = response.getStatusLine().getStatusCode();

                                if (statusCode >= 200 && statusCode < 300) {
                                    HttpEntity entity = response.getEntity();
                                    if (entity != null) {
                                        try (InputStream inputStream = entity.getContent();
                                             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                                            StringBuilder result = new StringBuilder();
                                            char[] buffer = new char[1024];
                                            int length;
                                            while ((length = reader.read(buffer)) != -1) {
                                                result.append(buffer, 0, length);
                                            }

                                            System.out.println(result);
                                            JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();
                                            String insertedId = jsonObject.get("insertedId").getAsString();

                                            updateTomlInZip(file, "remote", "tournamentId", insertedId);
                                        }
                                        DialogHelper.info("Sucessfully upload tournament");
                                    } else {
                                        error("Error  - no tournament ID returned");
                                    }
                                } else if (statusCode >= 400 && statusCode < 500) {
                                    error("Corrupted file - couldn't save on server");
                                } else if (statusCode >= 500 && statusCode < 600) {
                                    error("Internal server error");
                                } else {
                                    DialogHelper.warning("Unknown status code: " + statusCode);
                                }
                            } catch (SSLPeerUnverifiedException ex) {
                                error("Couldn't connect - insecure connection");
                            } catch (IOException ex) {
                                error("Connection error");
                            }
                        } else {
                            error("File not found");
                        }
                    }
                })
        );
        userTournaments.setOnAction(e -> DialogHelper.showUserTournaments(this));
        login.setOnAction(e -> DialogHelper.showLoginPopup());
        register.setOnAction(e -> DialogHelper.showRegisterPopup());
        exportPgnMenu.setOnAction(e ->
                DialogHelper.threeOptionsDialog("Export mode", "tournament", "round")
                        .thenAccept(choice -> {
                            if (choice.equals("A")) {
                                try {
                                    TournamentOperation.exportPgn(getTournament());
                                    DialogHelper.info("Exported games to pgn successfully");
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                    error("Error during exporting rounds");
                                }
                            } else if (choice.equals("B")) {
                                Integer currentRound = getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().getValue();
                                if (currentRound == null) {
                                    DialogHelper.warning("Nothing exported\nNo round has been selected");
                                } else {
                                    try {
                                        TournamentOperation.exportRoundPgn(getTournament().getRound(currentRound - 1), getTournament());
                                        DialogHelper.info("Export round %d successfully".formatted(currentRound));
                                    } catch (IOException ex) {
                                        System.out.println(ex.getMessage());
                                        error("Error during export round %d".formatted(currentRound));
                                    }
                                }
                            }
                        }));
        checkUpdates.setOnAction(e -> Platform.runLater(() -> {
            CommitViewer commitViewer = new CommitViewer();
            commitViewer.display();
        }));
        downloadFideMenu.setOnAction(e -> CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                .exceptionally(ex -> null));
        downloadFideButton.setOnAction(e -> CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                .exceptionally(ex -> null));
        downloadPolButton.setOnAction(e -> CompletableFuture.runAsync(FileOperation::downloadPolList)
                .exceptionally(ex -> null));

        randomTournament.setOnAction(e -> {
            try {
                if (getTournament().getSystem() == Tournament.TournamentSystem.SWISS) {
                    TournamentOperation.loadTournament(JavafoWrapper.generateRandomTournament(), this);
                } else if (getTournament().getSystem() == Tournament.TournamentSystem.ROUND_ROBIN) {
                    TournamentOperation.loadTournament(RoundRobinEngine.generateRandomTournament(), this);
                    getTourSystem().setValue(Tournament.TournamentSystem.ROUND_ROBIN);
                } else if (getTournament().getSystem() == Tournament.TournamentSystem.CUP) {
                    TournamentOperation.loadTournament(CupEngine.generateRandomTournament(), this);
                    getTourSystem().setValue(Tournament.TournamentSystem.CUP);
                }
                getTourNoRounds().setText(String.valueOf(getTournament().getRounds().size()));
                ArrayList<Integer> rounds = new ArrayList<>();
                for (int i = 1; i <= getTournament().getRoundsObs().size() + 1 && i <= getTournament().getRoundsNumber(); i++) {
                    rounds.add(i);
                }
                getRoundsHelper().getManualPairingHelper().setRoundsNumbersObs(FXCollections.observableArrayList(rounds));
                getRoundsHelper().getManualPairingHelper().getRoundUpdateSelect().setItems(getRoundsHelper().getManualPairingHelper().getRoundsNumbersObs());
            } catch (IOException | InterruptedException ex) {
                error("Couldn't generate random tournament");
            }
        });

        validateTournament.setOnAction(e -> {
            try {
                if (getTournament().getSystem() == Tournament.TournamentSystem.SWISS) {
                    JavafoWrapper.checkPairing(getTournament(), (byte) 0);
                } else if (getTournament().getSystem() == Tournament.TournamentSystem.ROUND_ROBIN) {
                    RoundRobinEngine.checkPairing(getTournament(), (byte) 0);
                }
            } catch (IOException | InterruptedException ex) {
                error("An error occurred during validate");
            }
        });

        roundValidation.setOnAction(e -> {
                    if (roundsViewSelect.getValue() == null) {
                        error("No round selected");
                    } else {
                        try {
                            if (getTournament().getSystem() == Tournament.TournamentSystem.SWISS) {
                                JavafoWrapper.checkPairing(getTournament(), roundsViewSelect.getValue().byteValue());
                            } else if (getTournament().getSystem() == Tournament.TournamentSystem.ROUND_ROBIN) {
                                RoundRobinEngine.checkPairing(getTournament(), roundsViewSelect.getValue().byteValue());
                            }
                        } catch (IOException | InterruptedException ex) {
                            error("An error occurred during validate");
                        }
                    }
                }
        );

        importTrf.setOnAction(e -> FIDEOperation.importTrfReport(this));

        importSwsx.setOnAction(e -> {
            File swsx = FileOperation.selectSwsx();
            if (swsx != null) {
                try {
                    SwsxTournament swsxTournament = new SwsxTournament(swsx);
                    Tournament tournament = new Tournament(swsxTournament);
                    TournamentOperation.loadTournament(tournament, this);
                    DialogHelper.info("Imported successfully");
                } catch (Exception ex) {
                    error("An error eccured");
                    System.out.println(ex.getMessage());
                }
            } else {
                DialogHelper.warning("No file selected");
            }
        });

        remoteSwsx.setOnAction(e -> DialogHelper.showRemoteChessarbiter(this));
        importPgn.setOnAction(e -> TournamentOperation.importPgn(this));
        closeMenu.setOnAction(e -> files.remove(getFile()));
        tournamentSelect.valueProperty().addListener((ObservableValue<? extends File> observable, File oldValue, File newValue) -> {
            if (newValue != oldValue && newValue != getFile() && newValue != null) {
                save(this);
            }
            if (newValue == null) {
                TournamentOperation.loadTournament(new Tournament(), this);
            } else {
                if (newValue != oldValue && newValue != getFile()) {
                    importJson(tournamentSelect.getValue(), this);
                }
            }
        });

        getTourTB1().valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getTiebreak().setTiebreak1(newValue);
            String tiebreak;
            if (newValue != null) {
                tiebreak = newValue.prettyText();
            } else {
                tiebreak = "";
            }
            playerCardTB1.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null && newValue != null) {
                playerCardTB1Value.setText(playerCardSelect.getValue().getTb1().toString());
            } else {
                playerCardTB1Value.setText("");
            }
            resultTb1.setText(tiebreak);
        });
        getTourTB2().valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getTiebreak().setTiebreak2(newValue);
            String tiebreak;
            if (newValue != null) {
                tiebreak = newValue.prettyText();
            } else {
                tiebreak = "";
            }
            playerCardTB2.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null && newValue != null) {
                playerCardTB2Value.setText(playerCardSelect.getValue().getTb2().toString());
            } else {
                playerCardTB2Value.setText("");
            }
            resultTb2.setText(tiebreak);
        });
        getTourTB3().valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getTiebreak().setTiebreak3(newValue);
            String tiebreak;
            if (newValue != null) {
                tiebreak = newValue.prettyText();
            } else {
                tiebreak = "";
            }
            playerCardTB3.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null && newValue != null) {
                playerCardTB3Value.setText(playerCardSelect.getValue().getTb3().toString());
            } else {
                playerCardTB3Value.setText("");
            }
            resultTb3.setText(tiebreak);
        });
        getTourTB4().valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getTiebreak().setTiebreak4(newValue);
            String tiebreak;
            if (newValue != null) {
                tiebreak = newValue.prettyText();
            } else {
                tiebreak = "";
            }
            playerCardTB4.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null && newValue != null) {
                playerCardTB4Value.setText(playerCardSelect.getValue().getTb4().toString());
            } else {
                playerCardTB4Value.setText("");
            }
            resultTb4.setText(tiebreak);
        });
        getTourTB5().valueProperty().addListener((ObservableValue<? extends Tournament.Tiebreak.TbMethod> observable, Tournament.Tiebreak.TbMethod oldValue, Tournament.Tiebreak.TbMethod newValue) -> {
            getTournament().getTiebreak().setTiebreak5(newValue);
            String tiebreak;
            if (newValue != null) {
                tiebreak = newValue.prettyText();
            } else {
                tiebreak = "";
            }
            playerCardTB5.setText(tiebreak);
            Player player = playerCardSelect.getValue();
            if (player != null && newValue != null) {
                playerCardTB5Value.setText(playerCardSelect.getValue().getTb5().toString());
            } else {
                playerCardTB5Value.setText("");
            }
            resultTb5.setText(tiebreak);
        });

        Thread tieBreakDaemonThread = getThread();
        tieBreakDaemonThread.start();
    }

    @NotNull
    private Thread getThread() {
        Thread daemonThread = new Thread(() -> {
            while (true) {
                try {
                    if (TieBreakServerWrapper.generateTiebreak(getTournament(), getTournament().calculateEndedRound())){
                        getTablesHelper().getResultTableHelper().getResultsTable().setItems(new SortedList<>(getTournament().getPlayersObs(), new ResultsComparator()));
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    error("Error in tie-break daemon - needed restart");
                    break;
                }
            }
        });

        daemonThread.setDaemon(true);
        return daemonThread;
    }

    public ObservableList<File> getFiles() {
        return files;
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
            if (file != null) {
                files.add(file);
            }
            tournamentSelect.setValue(file);
        }
    }

    public ComboBox<File> getTournamentSelect() {
        return tournamentSelect;
    }

    public MenuItem getNewMenu() {
        return newMenu;
    }

    public MenuItem getOpenMenu() {
        return openMenu;
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

    public Button getNewButton() {
        return newButton;
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

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB2() {
        return tourTB2;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB3() {
        return tourTB3;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB4() {
        return tourTB4;
    }

    public ComboBox<Tournament.Tiebreak.TbMethod> getTourTB5() {
        return tourTB5;
    }

    public TextField getPointsWin() {
        return pointsWin;
    }

    public TextField getPointsDraw() {
        return pointsDraw;
    }

    public TextField getPointsLose() {
        return pointsLose;
    }

    public TextField getPointsForfeitWin() {
        return pointsForfeitWin;
    }

    public TextField getPointsForfeitLose() {
        return pointsForfeitLose;
    }

    public TextField getPointsBye() {
        return pointsBye;
    }

    public TextField getPointsHalfBye() {
        return pointsHalfBye;
    }

    public TextField getMinInitGames() {
        return minInitGames;
    }

    public TextField getRatingFloor() {
        return ratingFloor;
    }

    public CheckBox getPZSzach43Cb() {
        return PZSzach43Cb;
    }

    public CheckBox getPZSzach44Cb() {
        return PZSzach44Cb;
    }

    public CheckBox getPZSzach45Cb() {
        return PZSzach45Cb;
    }


    public CheckBox getPZSzach46Cb() {
        return PZSzach46Cb;
    }

    public CheckBox getPZSzach47Cb() {
        return PZSzach47Cb;
    }

    public ComboBox<Title> getMaxTitle() {
        return maxTitle;
    }

    public CheckBox getTwoOtherFeds() {
        return twoOtherFeds;
    }

    public TextField getMinTitleGames() {
        return minTitleGames;
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

    public Tab getAllRoundsTab() {
        return allRoundsTab;
    }

    public TablesHelper getTablesHelper() {
        return tablesHelper;
    }

    public void setTablesHelper(TablesHelper tablesHelper) {
        this.tablesHelper = tablesHelper;
    }
}

