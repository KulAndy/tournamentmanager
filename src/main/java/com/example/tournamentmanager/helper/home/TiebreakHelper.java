package com.example.tournamentmanager.helper.home;

import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Tournament;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Method;

import static com.example.tournamentmanager.helper.GeneralHelper.*;

public class TiebreakHelper {
    private Tournament tournament;
    private CheckBox tourFIDEMode;
    private GridPane tiebreakPane;
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB1;
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB2;
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB3;
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB4;
    private ComboBox<Tournament.Tiebreak.TbMethod> tourTB5;
    private GridPane pointsPane;
    private TextField pointsWin;
    private TextField pointsDraw;
    private TextField pointsLose;
    private TextField pointsForfeitWin;
    private TextField pointsForfeitLose;
    private TextField pointsBye;
    private TextField pointsHalfBye;

    public TiebreakHelper(
            Tournament tournament,
            CheckBox tourFIDEMode, ComboBox<Tournament.Tiebreak.TbMethod> tourTB1, ComboBox<Tournament.Tiebreak.TbMethod> tourTB2,
            ComboBox<Tournament.Tiebreak.TbMethod> tourTB3, ComboBox<Tournament.Tiebreak.TbMethod> tourTB4, ComboBox<Tournament.Tiebreak.TbMethod> tourTB5,
            GridPane pointsPane, TextField pointsWin, TextField pointsDraw, TextField pointsLose, TextField pointsForfeitWin, TextField pointsForfeitLose,
            TextField pointsBye, TextField pointsHalfBye) {
        setTournament(tournament);
        setTourFIDEMode(tourFIDEMode);
        setTiebreakPane(tiebreakPane);
        setTourTB1(tourTB1);
        setTourTB2(tourTB2);
        setTourTB3(tourTB3);
        setTourTB4(tourTB4);
        setTourTB5(tourTB5);
        setPointsPane(pointsPane);
        setPointsWin(pointsWin);
        setPointsDraw(pointsDraw);
        setPointsLose(pointsLose);
        setPointsForfeitWin(pointsForfeitWin);
        setPointsForfeitLose(pointsForfeitLose);
        setPointsBye(pointsBye);
        setPointsHalfBye(pointsHalfBye);

        bindPointField(getPointsWin(), getTournament().getTiebreak(), "winPoints", "pointsForWin");
        bindPointField(getPointsDraw(), getTournament().getTiebreak(), "drawPoints", "pointsForDraw");
        bindPointField(getPointsLose(), getTournament().getTiebreak(), "losePoints", "pointsForLose");
        bindPointField(getPointsForfeitWin(), getTournament().getTiebreak(), "forfeitWinPoints", "pointsForForfeitWin");
        bindPointField(getPointsForfeitLose(), getTournament().getTiebreak(), "forfeitLosePoints", "pointsForForfeitLose");
        bindPointField(getPointsBye(), getTournament().getTiebreak(), "byePoints", "pointsForBye");
        bindPointField(getPointsHalfBye(), getTournament().getTiebreak(), "halfByePoints", "pointsForHalfBye");

        validateTextFieldFloat(getPointsWin());
        validateTextFieldFloat(getPointsDraw());
        validateTextFieldFloat(getPointsLose());
        validateTextFieldFloat(getPointsForfeitWin());
        validateTextFieldFloat(getPointsForfeitLose());
        validateTextFieldFloat(getPointsBye());
        validateTextFieldFloat(getPointsHalfBye());

        bindComboBox(getTourTB1(), getTournament().getTiebreak(), "tiebreak1", Tournament.Tiebreak.TbMethod.class);
        bindComboBox(getTourTB2(), getTournament().getTiebreak(), "tiebreak2", Tournament.Tiebreak.TbMethod.class);
        bindComboBox(getTourTB3(), getTournament().getTiebreak(), "tiebreak3", Tournament.Tiebreak.TbMethod.class);
        bindComboBox(getTourTB4(), getTournament().getTiebreak(), "tiebreak4", Tournament.Tiebreak.TbMethod.class);
        bindComboBox(getTourTB5(), getTournament().getTiebreak(), "tiebreak5", Tournament.Tiebreak.TbMethod.class);

        setupComboBox(getTourTB1(), Tournament.Tiebreak.TbMethod.values());
        setupComboBox(getTourTB2(), Tournament.Tiebreak.TbMethod.values());
        setupComboBox(getTourTB3(), Tournament.Tiebreak.TbMethod.values());
        setupComboBox(getTourTB4(), Tournament.Tiebreak.TbMethod.values());
        setupComboBox(getTourTB5(), Tournament.Tiebreak.TbMethod.values());

        setupFIDEMode();
        getTourFIDEMode().selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue != null) {
                if (newValue) {
                    getTiebreakPane().setDisable(true);
                    getPointsPane().setDisable(true);
                    setupFIDEMode();
                } else {
                    getTiebreakPane().setDisable(false);
                    getPointsPane().setDisable(false);
                }
            }
        });
    }

    public void bindPointField(TextField tf, Object obj, String tbAttr, String plAttr) {
        bindTextFieldFloat(tf, obj, tbAttr);
        try {
            Method setter = Player.class.getMethod("set" + Character.toUpperCase(plAttr.charAt(0)) + plAttr.substring(1), Float.class);
            Method getter1 = obj.getClass().getMethod("get" + Character.toUpperCase(tbAttr.charAt(0)) + tbAttr.substring(1));
            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    setter.invoke(null, getter1.invoke(obj));
                } catch (Exception ignored) {
                }
            });
        } catch (Exception ignored) {
        }
    }

    public void setupFIDEMode() {
        switch (getTournament().getSystem()) {
            case SWISS -> {
                getTourTB1().setValue(Tournament.Tiebreak.TbMethod.POINTS);
                getTourTB2().setValue(Tournament.Tiebreak.TbMethod.BUCHOLZ_CUT1);
                getTourTB3().setValue(Tournament.Tiebreak.TbMethod.BUCHOLZ);
                getTourTB4().setValue(Tournament.Tiebreak.TbMethod.SONNEN_BERGER);
                getTourTB5().setValue(Tournament.Tiebreak.TbMethod.PROGRESS);
                getPointsBye().setText("1");
                getPointsHalfBye().setText("0.5");
            }
            case ROUND_ROBIN -> {
                getTourTB1().setValue(Tournament.Tiebreak.TbMethod.POINTS);
                getTourTB2().setValue(Tournament.Tiebreak.TbMethod.DUEL);
                getTourTB3().setValue(Tournament.Tiebreak.TbMethod.WINS);
                getTourTB4().setValue(Tournament.Tiebreak.TbMethod.SONNEN_BERGER);
                getTourTB5().setValue(Tournament.Tiebreak.TbMethod.KOYA);
                getPointsBye().setText("0");
                getPointsHalfBye().setText("0");
            }
            case CUP -> {
                getTourTB1().setValue(Tournament.Tiebreak.TbMethod.DUEL);
                getTourTB2().setValue(null);
                getTourTB3().setValue(null);
                getTourTB4().setValue(null);
                getTourTB5().setValue(null);
                getPointsBye().setText("0");
                getPointsHalfBye().setText("0");
            }
        }
        getPointsWin().setText("1");
        getPointsDraw().setText("0.5");
        getPointsLose().setText("0");
        getPointsForfeitWin().setText("1");
        getPointsForfeitLose().setText("0");
    }

    public GridPane getTiebreakPane() {
        return tiebreakPane;
    }

    public void setTiebreakPane(GridPane tiebreakPane) {
        this.tiebreakPane = tiebreakPane;
    }

    public GridPane getPointsPane() {
        return pointsPane;
    }

    public void setPointsPane(GridPane pointsPane) {
        this.pointsPane = pointsPane;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public CheckBox getTourFIDEMode() {
        return tourFIDEMode;
    }

    public void setTourFIDEMode(CheckBox tourFIDEMode) {
        this.tourFIDEMode = tourFIDEMode;
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

}
