package com.example.tournamentmanager.helper.home;

import com.example.tournamentmanager.model.Title;
import com.example.tournamentmanager.model.Tournament;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static com.example.tournamentmanager.helper.GeneralHelper.*;

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
        validateTextFieldInt(getMinInitGames());
        validateTextFieldInt(getRatingFloor());
        validateTextFieldInt(getMinTitleGames());
        bindTextFieldInt(getMinInitGames(), getTournament().getRating(), "minInitGames", "byte");
        bindTextFieldInt(getRatingFloor(), getTournament().getRating(), "ratingFloor", "short");
        bindTextFieldInt(getMinTitleGames(), getTournament().getRating(), "minTitleGames", "byte");
        getMinInitGames().setText("5");
        getRatingFloor().setText("1000");
        getMinTitleGames().setText("9");

        bindComboBox(getMaxTitle(), getTournament().getRating(), "maxTitle", Title.class);
        setupComboBox(getMaxTitle(), Title.values());
        getMaxTitle().setValue(Title.M);

        bindCheckBoxRated(getPZSzach43Cb(), getTournament(), "PZSzach43");
        bindCheckBoxRated(getPZSzach44Cb(), getTournament(), "PZSzach44");
        bindCheckBoxRated(getPZSzach46Cb(), getTournament(), "PZSzach46");
        bindCheckBoxRated(getPZSzach47Cb(), getTournament(), "PZSzach47");
        bindCheckBoxRated(getTwoOtherFeds(), getTournament(), "twoOtherFederations");
        getPZSzach43Cb().setSelected(true);
        getPZSzach44Cb().setSelected(true);
        getPZSzach46Cb().setSelected(true);
        getPZSzach47Cb().setSelected(true);
        getTwoOtherFeds().setSelected(true);
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
