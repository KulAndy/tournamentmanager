package com.example.lolmanager.helper.home;

import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static com.example.lolmanager.helper.GeneralHelper.*;

public class RtgOptHelper {
    private Tournament tournament;
    private TextField minInitGames;
    private TextField ratingFloor;
    private CheckBox PZSzach43Cb;
    private CheckBox PZSzach44Cb;
    private CheckBox PZSzach46Cb;
    private CheckBox PZSzach47Cb;
    private ComboBox<Title> maxTitle;
    private CheckBox twoOtherFeds;
    private TextField minTitleGames;

    public RtgOptHelper(
            Tournament tournament,
            TextField minInitGames, TextField ratingFloor, CheckBox PZSzach43Cb, CheckBox PZSzach44Cb,
            CheckBox PZSzach46Cb, CheckBox PZSzach47Cb, ComboBox<Title> maxTitle, CheckBox twoOtherFeds,
            TextField minTitleGames) {
        setTournament(tournament);
        setMinInitGames(minInitGames);
        setRatingFloor(ratingFloor);
        setPZSzach43Cb(PZSzach43Cb);
        setPZSzach44Cb(PZSzach44Cb);
        setPZSzach46Cb(PZSzach46Cb);
        setPZSzach47Cb(PZSzach47Cb);
        setMaxTitle(maxTitle);
        setTwoOtherFeds(twoOtherFeds);
        setMinTitleGames(minTitleGames);
        validateTextFieldInt(minInitGames);
        validateTextFieldInt(ratingFloor);
        validateTextFieldInt(minTitleGames);
        bindTextFieldInt(minInitGames, tournament.getRating(), "minInitGames", "byte");
        bindTextFieldInt(ratingFloor, tournament.getRating(), "ratingFloor", "short");
        bindTextFieldInt(minTitleGames, tournament.getRating(), "minTitleGames", "byte");
        minInitGames.setText("5");
        ratingFloor.setText("1000");
        minTitleGames.setText("9");

        bindComboBox(maxTitle, tournament.getRating(), "maxTitle", Title.class);
        setupComboBox(maxTitle, Title.values());
        maxTitle.setValue(Title.M);

        bindCheckBoxRated(PZSzach43Cb, tournament, "PZSzach43");
        bindCheckBoxRated(PZSzach44Cb, tournament, "PZSzach44");
        bindCheckBoxRated(PZSzach46Cb, tournament, "PZSzach46");
        bindCheckBoxRated(PZSzach47Cb, tournament, "PZSzach47");
        bindCheckBoxRated(twoOtherFeds, tournament, "twoOtherFederations");
        PZSzach43Cb.setSelected(true);
        PZSzach44Cb.setSelected(true);
        PZSzach46Cb.setSelected(true);
        PZSzach47Cb.setSelected(true);
        twoOtherFeds.setSelected(true);
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
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


}
