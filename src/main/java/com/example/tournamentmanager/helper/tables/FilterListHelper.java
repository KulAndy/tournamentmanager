package com.example.tournamentmanager.helper.tables;

import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.ResultPredicate;
import com.example.tournamentmanager.model.Tournament;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FilterListHelper {
    private Tournament tournament;
    private TableView<ResultPredicate<Player>> filterListTable;
    private TableColumn<ResultPredicate<Player>, String> filterListName;
    private TableColumn<ResultPredicate<Player>, Player.Sex> filterListSex;
    private TableColumn<ResultPredicate<Player>, String> filterListYear;
    private TableColumn<ResultPredicate<Player>, String> filterListTitle;
    private TableColumn<ResultPredicate<Player>, String> filterListLocal;
    private TableColumn<ResultPredicate<Player>, String> filterListFideRtg;
    private TableColumn<ResultPredicate<Player>, String> filterListFed;
    private TableColumn<ResultPredicate<Player>, String> filterListState;
    private TableColumn<ResultPredicate<Player>, Void> filterListDelete;

    public FilterListHelper(
            Tournament tournament, TableView<ResultPredicate<Player>> filterListTable,
            TableColumn<ResultPredicate<Player>, String> filterListName, TableColumn<ResultPredicate<Player>, Player.Sex> filterListSex,
            TableColumn<ResultPredicate<Player>, String> filterListYear, TableColumn<ResultPredicate<Player>, String> filterListTitle,
            TableColumn<ResultPredicate<Player>, String> filterListLocal, TableColumn<ResultPredicate<Player>, String> filterListFideRtg,
            TableColumn<ResultPredicate<Player>, String> filterListFed, TableColumn<ResultPredicate<Player>, String> filterListState,
            TableColumn<ResultPredicate<Player>, Void> filterListDelete
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
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getYearCompareOperator() + " " + resultPredicate.getYear()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListTitle().setCellValueFactory(cellData -> {
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getTitleCompareOperator() + " " + resultPredicate.getTitle()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListLocal().setCellValueFactory(cellData -> {
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getLocalRtgCompareOperator() + " " + resultPredicate.getLocalRtg()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListFideRtg().setCellValueFactory(cellData -> {
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getFideRtgCompareOperator() + " " + resultPredicate.getFideRtg()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListFed().setCellValueFactory(cellData -> {
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getFedCompareOperator() + " " + resultPredicate.getFederation()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });
        getFilterListState().setCellValueFactory(cellData -> {
            ResultPredicate<Player> resultPredicate = cellData.getValue();
            String text = (resultPredicate.getStateCompareOperator() + " " + resultPredicate.getState()).replaceAll("null", "");
            return new SimpleStringProperty(text);
        });

        getFilterListDelete().setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    ResultPredicate<Player> resultPredicate = getTableRow().getItem();
                    if (resultPredicate != null) {
                        getTournament().getPredicatesObs().remove(resultPredicate);
                    }
                });
            }

            @Override
            private void updateItem(Void item, boolean empty) {
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

    public TableView<ResultPredicate<Player>> getFilterListTable() {
        return filterListTable;
    }

    public void setFilterListTable(TableView<ResultPredicate<Player>> filterListTable) {
        this.filterListTable = filterListTable;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListName() {
        return filterListName;
    }

    public void setFilterListName(TableColumn<ResultPredicate<Player>, String> filterListName) {
        this.filterListName = filterListName;
    }

    public TableColumn<ResultPredicate<Player>, Player.Sex> getFilterListSex() {
        return filterListSex;
    }

    public void setFilterListSex(TableColumn<ResultPredicate<Player>, Player.Sex> filterListSex) {
        this.filterListSex = filterListSex;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListYear() {
        return filterListYear;
    }

    public void setFilterListYear(TableColumn<ResultPredicate<Player>, String> filterListYear) {
        this.filterListYear = filterListYear;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListTitle() {
        return filterListTitle;
    }

    public void setFilterListTitle(TableColumn<ResultPredicate<Player>, String> filterListTitle) {
        this.filterListTitle = filterListTitle;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListLocal() {
        return filterListLocal;
    }

    public void setFilterListLocal(TableColumn<ResultPredicate<Player>, String> filterListLocal) {
        this.filterListLocal = filterListLocal;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListFideRtg() {
        return filterListFideRtg;
    }

    public void setFilterListFideRtg(TableColumn<ResultPredicate<Player>, String> filterListFideRtg) {
        this.filterListFideRtg = filterListFideRtg;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListFed() {
        return filterListFed;
    }

    public void setFilterListFed(TableColumn<ResultPredicate<Player>, String> filterListFed) {
        this.filterListFed = filterListFed;
    }

    public TableColumn<ResultPredicate<Player>, String> getFilterListState() {
        return filterListState;
    }

    public void setFilterListState(TableColumn<ResultPredicate<Player>, String> filterListState) {
        this.filterListState = filterListState;
    }

    public TableColumn<ResultPredicate<Player>, Void> getFilterListDelete() {
        return filterListDelete;
    }

    public void setFilterListDelete(TableColumn<ResultPredicate<Player>, Void> filterListDelete) {
        this.filterListDelete = filterListDelete;
    }
}
