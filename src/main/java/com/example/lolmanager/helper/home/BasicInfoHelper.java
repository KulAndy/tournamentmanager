package com.example.lolmanager.helper.home;

import com.example.lolmanager.model.Tournament;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static com.example.lolmanager.helper.GeneralHelper.*;

public class BasicInfoHelper {
    private Tournament tournament;
    private TextField tourName;
    private DatePicker tourStartDate;
    private DatePicker tourEndDate;
    private TextField tourPlace;
    private TextField tourGameTime;
    private TextField tourIncrement;
    private TextField tourControlMove;
    private TextField tourControlAddition;
    private ComboBox<Tournament.Type> tourType;
    private CheckBox tourRtPZSzach;
    private CheckBox tourRtFIDE;
    private TextField tourNoRounds;
    private ComboBox<Tournament.TournamentSystem> tourSystem;
    private TextField tourArbiter;
    private TextField tourOrganizer;
    private TextField tourEmail;

    public BasicInfoHelper(
            Tournament tournament, TextField tourName, DatePicker tourStartDate, DatePicker tourEndDate, TextField tourPlace, TextField tourGameTime,
            TextField tourIncrement, TextField tourControlMove, TextField tourControlAddition, ComboBox<Tournament.Type> tourType, CheckBox tourRtPZSzach, CheckBox tourRtFIDE,
            TextField tourNoRounds, ComboBox<Tournament.TournamentSystem> tourSystem, TextField tourArbiter, TextField tourOrganizer, TextField tourEmail
    ) {
        setTournament(tournament);
        setTourName(tourName);
        setTourStartDate(tourStartDate);
        setTourEndDate(tourEndDate);
        setTourPlace(tourPlace);
        setTourGameTime(tourGameTime);
        setTourIncrement(tourIncrement);
        setTourControlMove(tourControlMove);
        setTourControlAddition(tourControlAddition);
        setTourType(tourType);
        setTourRtPZSzach(tourRtPZSzach);
        setTourRtFIDE(tourRtFIDE);
        setTourNoRounds(tourNoRounds);
        setTourSystem(tourSystem);
        setTourArbiter(tourArbiter);
        setTourOrganizer(tourOrganizer);
        setTourEmail(tourEmail);
        try {
            bindTextFieldStringProperty(tourName, tournament, "name");
        } catch (Exception ignored) {
        }
        try {
            bindTextFieldStringProperty(tourPlace, tournament, "place");
        } catch (Exception ignored) {
        }
        try {
            bindTextFieldStringProperty(tourArbiter, tournament, "arbiter");
        } catch (Exception ignored) {
        }
        try {
            bindTextFieldStringProperty(tourOrganizer, tournament, "organizer");
        } catch (Exception ignored) {
        }
        try {
            bindTextFieldStringProperty(tourEmail, tournament, "email");
        } catch (Exception ignored) {
        }
        validateTextFieldInt(tourGameTime);
        validateTextFieldInt(tourIncrement);
        validateTextFieldInt(tourControlMove);
        validateTextFieldInt(tourControlAddition);
        validateTextFieldInt(tourNoRounds);
        bindTextFieldInt(tourGameTime, tournament, "gameTime", "short");
        bindTextFieldInt(tourIncrement, tournament, "increment", "short");
        bindTextFieldInt(tourControlMove, tournament, "controlMove", "byte");
        bindTextFieldInt(tourControlAddition, tournament, "controlAddition", "byte");
        bindTextFieldInt(tourNoRounds, tournament, "roundsNumber", "byte");

        bindDatePicker(tourStartDate, tournament, "startDate");
        bindDatePicker(tourEndDate, tournament, "endDate");
        tourStartDate.setValue(LocalDate.now());
        tourEndDate.setValue(LocalDate.now());

        bindComboBox(tourType, tournament, "type", Tournament.Type.class);
        setupComboBox(tourType, Tournament.Type.values());
        setupComboBox(tourSystem, Tournament.TournamentSystem.values());
        bindCheckBoxRated(tourRtPZSzach, tournament, "PZSzachRated");
        bindCheckBoxRated(tourRtFIDE, tournament, "FIDERated");

    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TextField getTourName() {
        return tourName;
    }

    public void setTourName(TextField tourName) {
        this.tourName = tourName;
    }

    public DatePicker getTourStartDate() {
        return tourStartDate;
    }

    public void setTourStartDate(DatePicker tourStartDate) {
        this.tourStartDate = tourStartDate;
    }

    public DatePicker getTourEndDate() {
        return tourEndDate;
    }

    public void setTourEndDate(DatePicker tourEndDate) {
        this.tourEndDate = tourEndDate;
    }

    public TextField getTourPlace() {
        return tourPlace;
    }

    public void setTourPlace(TextField tourPlace) {
        this.tourPlace = tourPlace;
    }

    public TextField getTourGameTime() {
        return tourGameTime;
    }

    private void setTourGameTime(short time) {
        tourGameTime.setText(String.valueOf(time));
        tournament.setGameTime(time);
    }

    public void setTourGameTime(TextField tourGameTime) {
        this.tourGameTime = tourGameTime;
    }

    public TextField getTourIncrement() {
        return tourIncrement;
    }

    public void setTourIncrement(TextField tourIncrement) {
        this.tourIncrement = tourIncrement;
    }

    public TextField getTourControlMove() {
        return tourControlMove;
    }

    public void setTourControlMove(TextField tourControlMove) {
        this.tourControlMove = tourControlMove;
    }

    public TextField getTourControlAddition() {
        return tourControlAddition;
    }

    public void setTourControlAddition(TextField tourControlAddition) {
        this.tourControlAddition = tourControlAddition;
    }

    public ComboBox<Tournament.Type> getTourType() {
        return tourType;
    }

    public void setTourType(ComboBox<Tournament.Type> tourType) {
        this.tourType = tourType;
    }

    public CheckBox getTourRtPZSzach() {
        return tourRtPZSzach;
    }

    public void setTourRtPZSzach(CheckBox tourRtPZSzach) {
        this.tourRtPZSzach = tourRtPZSzach;
    }

    public CheckBox getTourRtFIDE() {
        return tourRtFIDE;
    }

    public void setTourRtFIDE(CheckBox tourRtFIDE) {
        this.tourRtFIDE = tourRtFIDE;
    }

    public TextField getTourNoRounds() {
        return tourNoRounds;
    }

    public void setTourNoRounds(TextField tourNoRounds) {
        this.tourNoRounds = tourNoRounds;
    }

    public ComboBox<Tournament.TournamentSystem> getTourSystem() {
        return tourSystem;
    }

    public void setTourSystem(ComboBox<Tournament.TournamentSystem> tourSystem) {
        this.tourSystem = tourSystem;
    }

    public TextField getTourArbiter() {
        return tourArbiter;
    }

    public void setTourArbiter(TextField tourArbiter) {
        this.tourArbiter = tourArbiter;
    }

    public TextField getTourOrganizer() {
        return tourOrganizer;
    }

    public void setTourOrganizer(TextField tourOrganizer) {
        this.tourOrganizer = tourOrganizer;
    }

    public TextField getTourEmail() {
        return tourEmail;
    }

    public void setTourEmail(TextField tourEmail) {
        this.tourEmail = tourEmail;
    }

}
