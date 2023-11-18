package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.*;
import com.example.lolmanager.operation.FileOperation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static com.example.lolmanager.helper.GeneralHelper.error;
import static com.example.lolmanager.helper.GeneralHelper.setupComboBox;

public class FilterCreatorHelper {
    private Tournament tournament;
    private TextField filterNameField;
    private Button filterCreate;
    private ComboBox<Player.Sex> filterSexSelect;
    private ComboBox<ResultPredicate.CompareOperator> filterYearOperator;
    private TextField filterYearField;
    private ComboBox<ResultPredicate.CompareOperator> filterTitleOperator;
    private ComboBox<Title> filterTitleSelect;
    private ComboBox<ResultPredicate.CompareOperator> filterLocalRtgOperator;
    private TextField filterLocalRtg;
    private ComboBox<ResultPredicate.CompareOperator> filterFIDERtgOperator;
    private TextField filterFIDERtg;
    private ComboBox<ResultPredicate.CompareOperator> filterFedOperator;
    private ComboBox<Federation> filterFedSelect;
    private ComboBox<ResultPredicate.CompareOperator> filterStateOperator;
    private ComboBox<String> filterStateSelect;

    public FilterCreatorHelper(
            Tournament tournament,
            TextField filterNameField, Button filterCreate, ComboBox<Player.Sex> filterSexSelect,
            ComboBox<ResultPredicate.CompareOperator> filterYearOperator, TextField filterYearField,
            ComboBox<ResultPredicate.CompareOperator> filterTitleOperator, ComboBox<Title> filterTitleSelect,
            ComboBox<ResultPredicate.CompareOperator> filterLocalRtgOperator, TextField filterLocalRtg,
            ComboBox<ResultPredicate.CompareOperator> filterFIDERtgOperator, TextField filterFIDERtg,
            ComboBox<ResultPredicate.CompareOperator> filterFedOperator, ComboBox<Federation> filterFedSelect,
            ComboBox<ResultPredicate.CompareOperator> filterStateOperator, ComboBox<String> filterStateSelect
    ) {
        setTournament(tournament);
        setFilterNameField(filterNameField);
        setFilterCreate(filterCreate);
        setFilterSexSelect(filterSexSelect);
        setFilterYearOperator(filterYearOperator);
        setFilterYearField(filterYearField);
        setFilterTitleOperator(filterTitleOperator);
        setFilterTitleSelect(filterTitleSelect);
        setFilterLocalRtgOperator(filterLocalRtgOperator);
        setFilterLocalRtg(filterLocalRtg);
        setFilterFIDERtgOperator(filterFIDERtgOperator);
        setFilterFIDERtg(filterFIDERtg);
        setFilterFedOperator(filterFedOperator);
        setFilterFedSelect(filterFedSelect);
        setFilterStateOperator(filterStateOperator);
        setFilterStateSelect(filterStateSelect);

        ObservableList<ResultPredicate.CompareOperator> allOperators = FXCollections.observableArrayList(ResultPredicate.CompareOperator.values());
        allOperators.add(0, null);
        ObservableList<ResultPredicate.CompareOperator> equalOperators = FXCollections.observableArrayList(ResultPredicate.CompareOperator.EQUAL, ResultPredicate.CompareOperator.UNEQUAL);
        equalOperators.add(0, null);

        getFilterSexSelect().setItems(FXCollections.observableArrayList(Player.Sex.values()));
        getFilterSexSelect().getItems().add(0, null);
        getFilterYearOperator().setItems(allOperators);
        getFilterTitleOperator().setItems(allOperators);
        getFilterTitleSelect().setItems(FXCollections.observableArrayList(Title.values()));
        getFilterTitleSelect().getItems().add(0, null);
        getFilterLocalRtgOperator().setItems(allOperators);
        getFilterFIDERtgOperator().setItems(allOperators);
        getFilterFedOperator().setItems(equalOperators);
        getFilterFedSelect().setItems(FXCollections.observableArrayList(Federation.values()));
        getFilterFedSelect().getItems().add(0, null);
        getFilterStateOperator().setItems(equalOperators);

        getFilterFedSelect().valueProperty().addListener((ObservableValue<? extends Federation> observable, Federation oldValue, Federation newValue) -> {
            if (newValue == null || getFilterFedOperator().getValue() != ResultPredicate.CompareOperator.EQUAL){
                getFilterStateSelect().getItems().clear();
            } else{
                String[] provinces = FileOperation.searchProvince(newValue);
                getFilterStateSelect().getItems().clear();
                setupComboBox(getFilterStateSelect(), provinces);
                getFilterStateSelect().getItems().add(0, null);
                getFilterStateSelect().setValue(null);
            }
        });
        getFilterFedOperator().valueProperty().addListener((ObservableValue<? extends ResultPredicate.CompareOperator> observable, ResultPredicate.CompareOperator oldValue, ResultPredicate.CompareOperator newValue) -> {
            if (newValue == null || getFilterFedSelect().getValue() != null){
                getFilterStateSelect().getItems().clear();
            } else{
                String[] provinces = FileOperation.searchProvince(getFilterFedSelect().getValue());
                getFilterStateSelect().getItems().clear();
                setupComboBox(getFilterStateSelect(), provinces);
                getFilterStateSelect().getItems().add(0, null);
                getFilterStateSelect().setValue(null);
            }});


        getFilterCreate().setOnAction(e->{
            String name = getFilterNameField().getText();
            if (name.isEmpty()){
                error("Name cannot be empty");
                return;
            }
            Player.Sex sex = getFilterSexSelect().getValue();
            ResultPredicate.CompareOperator yearOperator = getFilterYearOperator().getValue();
            Integer year = null;
            try{
                year = Integer.parseInt(getFilterYearField().getText());
            } catch (NumberFormatException ignored) {}
            ResultPredicate.CompareOperator titleOperator = getFilterTitleOperator().getValue();
            Title title = getFilterTitleSelect().getValue();
            ResultPredicate.CompareOperator localRtgOperator = getFilterLocalRtgOperator().getValue();
            Integer localRtg = null;
            try{
                localRtg = Integer.parseInt(getFilterLocalRtg().getText());
            } catch (NumberFormatException ignored) {}
            ResultPredicate.CompareOperator fideRtgOperator = getFilterFIDERtgOperator().getValue();
            Integer fideRtg = null;
            try{
                fideRtg = Integer.parseInt(getFilterFIDERtg().getText());
            } catch (NumberFormatException ignored) {}
            ResultPredicate.CompareOperator fedOperator = getFilterFedOperator().getValue();
            Federation federation = getFilterFedSelect().getValue();
            ResultPredicate.CompareOperator stateOperator = getFilterStateOperator().getValue();
            String state = getFilterStateSelect().getValue();


            ResultPredicate<Player> predicate = new ResultPredicate<>(
                    name, sex, yearOperator, year, titleOperator, title, localRtgOperator, localRtg,
                    fideRtgOperator, fideRtg, fedOperator, federation, stateOperator, state
            );
            getTournament().getPredicatesObs().add(predicate);

            getFilterNameField().setText("");
            getFilterSexSelect().setValue(null);
            getFilterYearOperator().setValue(null);
            getFilterYearField().setText("");
            getFilterTitleOperator().setValue(null);
            getFilterTitleSelect().setValue(null);
            getFilterLocalRtgOperator().setValue(null);
            getFilterLocalRtg().setText("");
            getFilterFIDERtgOperator().setValue(null);
            getFilterFIDERtg().setText("");
            getFilterFedOperator().setValue(null);
            getFilterFedSelect().setValue(null);
            getFilterStateOperator().setValue(null);
            getFilterStateSelect().setValue(null);

        });

    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TextField getFilterNameField() {
        return filterNameField;
    }

    public void setFilterNameField(TextField filterNameField) {
        this.filterNameField = filterNameField;
    }

    public Button getFilterCreate() {
        return filterCreate;
    }

    public void setFilterCreate(Button filterCreate) {
        this.filterCreate = filterCreate;
    }

    public ComboBox<Player.Sex> getFilterSexSelect() {
        return filterSexSelect;
    }

    public void setFilterSexSelect(ComboBox<Player.Sex> filterSexSelect) {
        this.filterSexSelect = filterSexSelect;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterYearOperator() {
        return filterYearOperator;
    }

    public void setFilterYearOperator(ComboBox<ResultPredicate.CompareOperator> filterYearOperator) {
        this.filterYearOperator = filterYearOperator;
    }

    public TextField getFilterYearField() {
        return filterYearField;
    }

    public void setFilterYearField(TextField filterYearField) {
        this.filterYearField = filterYearField;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterTitleOperator() {
        return filterTitleOperator;
    }

    public void setFilterTitleOperator(ComboBox<ResultPredicate.CompareOperator> filterTitleOperator) {
        this.filterTitleOperator = filterTitleOperator;
    }

    public ComboBox<Title> getFilterTitleSelect() {
        return filterTitleSelect;
    }

    public void setFilterTitleSelect(ComboBox<Title> filterTitleSelect) {
        this.filterTitleSelect = filterTitleSelect;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterLocalRtgOperator() {
        return filterLocalRtgOperator;
    }

    public void setFilterLocalRtgOperator(ComboBox<ResultPredicate.CompareOperator> filterLocalRtgOperator) {
        this.filterLocalRtgOperator = filterLocalRtgOperator;
    }

    public TextField getFilterLocalRtg() {
        return filterLocalRtg;
    }

    public void setFilterLocalRtg(TextField filterLocalRtg) {
        this.filterLocalRtg = filterLocalRtg;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterFIDERtgOperator() {
        return filterFIDERtgOperator;
    }

    public void setFilterFIDERtgOperator(ComboBox<ResultPredicate.CompareOperator> filterFIDERtgOperator) {
        this.filterFIDERtgOperator = filterFIDERtgOperator;
    }

    public TextField getFilterFIDERtg() {
        return filterFIDERtg;
    }

    public void setFilterFIDERtg(TextField filterFIDERtg) {
        this.filterFIDERtg = filterFIDERtg;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterFedOperator() {
        return filterFedOperator;
    }

    public void setFilterFedOperator(ComboBox<ResultPredicate.CompareOperator> filterFedOperator) {
        this.filterFedOperator = filterFedOperator;
    }

    public ComboBox<Federation> getFilterFedSelect() {
        return filterFedSelect;
    }

    public void setFilterFedSelect(ComboBox<Federation> filterFedSelect) {
        this.filterFedSelect = filterFedSelect;
    }

    public ComboBox<ResultPredicate.CompareOperator> getFilterStateOperator() {
        return filterStateOperator;
    }

    public void setFilterStateOperator(ComboBox<ResultPredicate.CompareOperator> filterStateOperator) {
        this.filterStateOperator = filterStateOperator;
    }

    public ComboBox<String> getFilterStateSelect() {
        return filterStateSelect;
    }

    public void setFilterStateSelect(ComboBox<String> filterStateSelect) {
        this.filterStateSelect = filterStateSelect;
    }

}
