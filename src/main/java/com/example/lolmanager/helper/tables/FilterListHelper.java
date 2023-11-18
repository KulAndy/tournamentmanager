package com.example.lolmanager.helper.tables;

import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.ResultPredicate;
import com.example.lolmanager.model.Tournament;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.function.Predicate;

public class FilterListHelper {
    private Tournament tournament;
    private TableView<ResultPredicate> filterListTable;
    private TableColumn<ResultPredicate, String> filterListName;
    private TableColumn<ResultPredicate, Player.Sex> filterListSex;
    private TableColumn<ResultPredicate, String> filterListYear;
    private TableColumn<ResultPredicate, String> filterListTitle;
    private TableColumn<ResultPredicate, String> filterListLocal;
    private TableColumn<ResultPredicate, String> filterListFideRtg;
    private TableColumn<ResultPredicate, String> filterListFed;
    private TableColumn<ResultPredicate, String> filterListState;
    private TableColumn<ResultPredicate, Void> filterListDelete;

    public FilterListHelper(
            Tournament tournament, TableView<ResultPredicate> filterListTable,
            TableColumn<ResultPredicate, String> filterListName, TableColumn<ResultPredicate, Player.Sex> filterListSex,
            TableColumn<ResultPredicate, String> filterListYear, TableColumn<ResultPredicate, String> filterListTitle,
            TableColumn<ResultPredicate, String> filterListLocal, TableColumn<ResultPredicate, String> filterListFideRtg,
            TableColumn<ResultPredicate, String> filterListFed, TableColumn<ResultPredicate, String> filterListState,
            TableColumn<ResultPredicate, Void> filterListDelete
    ) {
        setTournament(tournament);
        setFilterListTable(filterListTable);
        setFilterListName(filterListName);
        setFilterListSex(filterListSex);
        setFilterListYear(filterListYear);
        setFilterListTitle(filterListTitle);
        setFilterListLocal(filterListLocal);
        setFilterListFideRtg(filterListFideRtg);
        setFilterListFed(filterListFed);
        setFilterListState(filterListState);
        setFilterListDelete(filterListDelete);

        getFilterListName().setCellValueFactory(new PropertyValueFactory<>("filterName"));
        getFilterListSex().setCellValueFactory(new PropertyValueFactory<>("sex"));

        getFilterListYear().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getYearCompareOperator() + " " + resultPredicate.getYear()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListTitle().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getTitleCompareOperator() + " " + resultPredicate.getTitle()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListLocal().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getLocalRtgCompareOperator() + " " + resultPredicate.getLocalRtg()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListFideRtg().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getFideRtgCompareOperator() + " " + resultPredicate.getFideRtg()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListFed().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getFedCompareOperator() + " " + resultPredicate.getFederation()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListState().setCellValueFactory(cellData -> {
            ResultPredicate resultPredicate = cellData.getValue();
            String text = (resultPredicate.getStateCompareOperator() + " " + resultPredicate.getState()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });

        getFilterListDelete().setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    ResultPredicate resultPredicate = getTableRow().getItem();
                    if (resultPredicate != null) {
                        getTournament().getPredicatesObs().remove(resultPredicate);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(deleteButton);
                } else {
                    setGraphic(null);
                }
            }
        });

    getFilterListTable().setItems(getTournament().getPredicatesObs());

    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TableView<ResultPredicate> getFilterListTable() {
        return filterListTable;
    }

    public void setFilterListTable(TableView<ResultPredicate> filterListTable) {
        this.filterListTable = filterListTable;
    }

    public TableColumn<ResultPredicate, String> getFilterListName() {
        return filterListName;
    }

    public void setFilterListName(TableColumn<ResultPredicate, String> filterListName) {
        this.filterListName = filterListName;
    }

    public TableColumn<ResultPredicate, Player.Sex> getFilterListSex() {
        return filterListSex;
    }

    public void setFilterListSex(TableColumn<ResultPredicate, Player.Sex> filterListSex) {
        this.filterListSex = filterListSex;
    }

    public TableColumn<ResultPredicate, String> getFilterListYear() {
        return filterListYear;
    }

    public void setFilterListYear(TableColumn<ResultPredicate, String> filterListYear) {
        this.filterListYear = filterListYear;
    }

    public TableColumn<ResultPredicate, String> getFilterListTitle() {
        return filterListTitle;
    }

    public void setFilterListTitle(TableColumn<ResultPredicate, String> filterListTitle) {
        this.filterListTitle = filterListTitle;
    }

    public TableColumn<ResultPredicate, String> getFilterListLocal() {
        return filterListLocal;
    }

    public void setFilterListLocal(TableColumn<ResultPredicate, String> filterListLocal) {
        this.filterListLocal = filterListLocal;
    }

    public TableColumn<ResultPredicate, String> getFilterListFideRtg() {
        return filterListFideRtg;
    }

    public void setFilterListFideRtg(TableColumn<ResultPredicate, String> filterListFideRtg) {
        this.filterListFideRtg = filterListFideRtg;
    }

    public TableColumn<ResultPredicate, String> getFilterListFed() {
        return filterListFed;
    }

    public void setFilterListFed(TableColumn<ResultPredicate, String> filterListFed) {
        this.filterListFed = filterListFed;
    }

    public TableColumn<ResultPredicate, String> getFilterListState() {
        return filterListState;
    }

    public void setFilterListState(TableColumn<ResultPredicate, String> filterListState) {
        this.filterListState = filterListState;
    }

    public TableColumn<ResultPredicate, Void> getFilterListDelete() {
        return filterListDelete;
    }

    public void setFilterListDelete(TableColumn<ResultPredicate, Void> filterListDelete) {
        this.filterListDelete = filterListDelete;
    }
}
