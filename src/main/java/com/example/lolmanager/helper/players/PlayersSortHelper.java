package com.example.lolmanager.helper.players;

import com.example.lolmanager.comparator.StartListComparator;
import com.example.lolmanager.model.Tournament;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import static com.example.lolmanager.helper.GeneralHelper.bindComboBox;
import static com.example.lolmanager.helper.GeneralHelper.setupComboBox;

public class PlayersSortHelper {
    private Tournament tournament;
    private ComboBox<StartListComparator.SortCriteria> criteria1;
    private ComboBox<StartListComparator.SortCriteria> criteria2;
    private ComboBox<StartListComparator.SortCriteria> criteria3;
    private ComboBox<StartListComparator.SortCriteria> criteria4;
    private ComboBox<StartListComparator.SortCriteria> criteria5;
    private Button applySortButton;

    public PlayersSortHelper(
            Tournament tournament,
            ComboBox<StartListComparator.SortCriteria> criteria1,
            ComboBox<StartListComparator.SortCriteria> criteria2,
            ComboBox<StartListComparator.SortCriteria> criteria3,
            ComboBox<StartListComparator.SortCriteria> criteria4,
            ComboBox<StartListComparator.SortCriteria> criteria5,
            Button applySortButton) {
        setTournament(tournament);
        setCriteria1(criteria1);
        setCriteria2(criteria2);
        setCriteria3(criteria3);
        setCriteria4(criteria4);
        setCriteria5(criteria5);
        setApplySortButton(applySortButton);

        setupComboBox(criteria1, StartListComparator.SortCriteria.values());
        setupComboBox(criteria2, StartListComparator.SortCriteria.values());
        setupComboBox(criteria3, StartListComparator.SortCriteria.values());
        setupComboBox(criteria4, StartListComparator.SortCriteria.values());
        setupComboBox(criteria5, StartListComparator.SortCriteria.values());

        bindComboBox(criteria1, tournament.getPlayers().getComparator(), "criteria1", StartListComparator.SortCriteria.class);
        bindComboBox(criteria2, tournament.getPlayers().getComparator(), "criteria2", StartListComparator.SortCriteria.class);
        bindComboBox(criteria3, tournament.getPlayers().getComparator(), "criteria3", StartListComparator.SortCriteria.class);
        bindComboBox(criteria4, tournament.getPlayers().getComparator(), "criteria4", StartListComparator.SortCriteria.class);
        bindComboBox(criteria5, tournament.getPlayers().getComparator(), "criteria5", StartListComparator.SortCriteria.class);

        criteria1.setValue(StartListComparator.SortCriteria.FIDE_RATING);
        criteria2.setValue(StartListComparator.SortCriteria.LOCAL_RATING);
        criteria3.setValue(StartListComparator.SortCriteria.TITLE);
        criteria4.setValue(StartListComparator.SortCriteria.ALPHABETIC);
        criteria5.setValue(StartListComparator.SortCriteria.RANDOM);

        getApplySortButton().setOnAction(event -> {
            getTournament().getPlayers().sort();
            FXCollections.sort(getTournament().getPlayersObs(), getTournament().getPlayers().getComparator());
        });
    }


    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public ComboBox<StartListComparator.SortCriteria> getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(ComboBox<StartListComparator.SortCriteria> criteria1) {
        this.criteria1 = criteria1;
    }

    public ComboBox<StartListComparator.SortCriteria> getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(ComboBox<StartListComparator.SortCriteria> criteria2) {
        this.criteria2 = criteria2;
    }

    public ComboBox<StartListComparator.SortCriteria> getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(ComboBox<StartListComparator.SortCriteria> criteria3) {
        this.criteria3 = criteria3;
    }

    public ComboBox<StartListComparator.SortCriteria> getCriteria4() {
        return criteria4;
    }

    public void setCriteria4(ComboBox<StartListComparator.SortCriteria> criteria4) {
        this.criteria4 = criteria4;
    }

    public ComboBox<StartListComparator.SortCriteria> getCriteria5() {
        return criteria5;
    }

    public void setCriteria5(ComboBox<StartListComparator.SortCriteria> criteria5) {
        this.criteria5 = criteria5;
    }

    public Button getApplySortButton() {
        return applySortButton;
    }

    public void setApplySortButton(Button applySortButton) {
        this.applySortButton = applySortButton;
    }


}
