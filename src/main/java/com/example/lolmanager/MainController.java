package com.example.lolmanager;

import com.example.lolmanager.comparator.StartListComparator;
import com.example.lolmanager.helper.*;
import com.example.lolmanager.model.*;
import com.example.lolmanager.operation.ExcelOperation;
import com.example.lolmanager.operation.FIDEOperation;
import com.example.lolmanager.operation.FileOperation;
import com.example.lolmanager.operation.TournamentOperation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.example.lolmanager.helper.GeneralHelper.error;
import static com.example.lolmanager.helper.GeneralHelper.info;

public class MainController implements Initializable {
    @FXML
    ScrollPane allRoundsScroll;
    private String programName = "";
    private String programExtension = "*";
    private Tournament tournament;
    private File file;
    private FileOperation fileOperation;
    private ShortcutsHelper shortcutsHelper;
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
    private Button firstRoundBth;
    @FXML
    private Button previousRoundBth;
    @FXML
    private Button nextRoundBth;
    @FXML
    private Button lastRoundBth;
    @FXML
    private Button downloadFideButton;
    @FXML
    private Button downloadPolButton;
    @FXML
    private TabPane mainTabPane;
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
    private PlayersHelper playersHelper;
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
    private Button applyResultButton;
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
    private ComboBox resultFilter;
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
    private TableColumn<Player, Float> resultPoints;
    @FXML
    private TableColumn<Player, Float> resultBuchCut;
    @FXML
    private TableColumn<Player, Float> resultBuch;
    @FXML
    private TableColumn<Player, Float> resultBerger;
    @FXML
    private TableColumn<Player, Float> resultProgress;


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
                minInitGames, ratingFloor, PZSzach43Cb, PZSzach44Cb, PZSzach46Cb, PZSzach47Cb, maxTitle, twoOtherFeds, minTitleGames
        );
        playersHelper = new PlayersHelper(
                tournament, playersListTable,
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
                addPlayerButton, updatePlayerBth ,clearPlayerButton, addClearPlayerButton,
                insertFromList, newPlayerHint
        );

        roundsHelper = new RoundsHelper(
                tournament, roundUpdateSelect, whiteSearch, blackSearch, whiteList, blackList,
                autoColorCheckbox, pairRestButton, pairButton, whithdrawButton, byePairButton, halfByePairButton,
                clearManualButton, unpairButton, swapColorPairButton,
                applyManualButton, pairsList, roundsViewSelect, firstRound, previousRound, nextRound, lastRound, whiteWinResult, drawResult,
                blackWinResult, whiteWinForfeitResult, blackWinForfeitResult, applyResultButton, gamesView,
                leftBoardNo, whitePoints, whiteRating, whitePlayer, gameResult, blackPlayer,
                blackRating, blackPoints, rightBoardNo, deleteRound, enginePairButton,
                allRoundsScroll,
                withdrawPlayerSelect,  withdrawTypeSelect, withdrawRound, acceptWithdrawButton, withdrawTable,
                withdrawNoCol, withdrawNameCol, withdrawTypeCol, withdrawRoundCol, withdrawBackCol

        );

        tablesHelper = new TablesHelper(
                tournament, rtgPolTable, rtPolId, rtPolTitle, rtPolName, rtPolGames, rtPolPoints, rtPolAverage, rtPolPerformance, rtPolNorm, rtPolRemarks,
                rtgFideTable, rtgFideName, rtgFideId, rtgFideTitle, rtgFideFed, rtgFideElo, rtgFidePoints, rtgFideGames, rtgFideAverage, rtgFideChg, rtgFideNorm,
                resultFiltered, resultFilter, resultsTable,
                resultPlace, resultStartNo, resultTitle, resultName,
                resultElo, resultLocal, resultFed, resultPoints,
                resultBuchCut, resultBuch, resultBerger, resultProgress
        );
    }

    public void init(Scene scene, String programName, String programExtension) {
        setProgramName(programName);
        setProgramExtension(programExtension);
        setFileOperation(new FileOperation(this));
        setupEvents();
        setShortcutsHelper(new ShortcutsHelper(scene, getFileOperation(), roundsHelper, roundsTab, enterResultsTab));

    }

    public void setupEvents() {
        quitMenu.setOnAction(e -> quit());
        saveAsMenu.setOnAction(e -> getFileOperation().saveAs());
        saveMenu.setOnAction(e -> getFileOperation().save());
        saveButton.setOnAction(e -> getFileOperation().save());
        openMenu.setOnAction(e -> getFileOperation().open());
        openButton.setOnAction(e -> getFileOperation().open());
        fideReg.setOnAction(e -> ExcelOperation.createApplication(tournament, programName));
        trfRaport.setOnAction(e -> FIDEOperation.selectTrfReport(getTournament()));
        downloadFideMenu.setOnAction(e -> {
            CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
        });
        downloadFideButton.setOnAction(e -> {
            CompletableFuture.runAsync(FIDEOperation::downloadFIDEList)
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
        });
        downloadPolButton.setOnAction(e -> {
            CompletableFuture.runAsync(FileOperation::downloadPolList)
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
        });

        importTrf.setOnAction(e -> FIDEOperation.importTrfReport(this));

        importSwsx.setOnAction(e -> {
            File swsx = FileOperation.selectSwsx();
            try {
                if (swsx != null) {
                    SwsxTournament swsxTournament = new SwsxTournament(swsx);
                    Tournament tournament = new Tournament(swsxTournament);
                    TournamentOperation.loadTournament(tournament, this);
                    info("Imported successfully");
                } else {
                    error("An error eccured");
                }
            } catch (Exception ex) {
                error("An error eccured");
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
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

}

