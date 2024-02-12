package com.example.tournamentmanager.helper.home;

import com.example.tournamentmanager.model.Schedule;
import com.example.tournamentmanager.model.Tournament;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class ScheduleHelper {
    private Tournament tournament;
    private TableView<Schedule.ScheduleElement> scheduleTable;
    private TableColumn<Schedule.ScheduleElement, String> scheduleName;
    private TableColumn<Schedule.ScheduleElement, Void> scheduleDate;

    public ScheduleHelper(
            Tournament tournament,
            TableView<Schedule.ScheduleElement> scheduleTable,
            TableColumn<Schedule.ScheduleElement, String> scheduleName,
            TableColumn<Schedule.ScheduleElement, Void> scheduleDate
    ) {
        setTournament(tournament);
        setScheduleTable(scheduleTable);
        setScheduleName(scheduleName);
        setScheduleDate(scheduleDate);

        getScheduleName().setCellValueFactory(new PropertyValueFactory<>("eventName"));
        getScheduleDate().setCellFactory(param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                    Schedule.ScheduleElement element = getTableRow().getItem();
                    if (element != null) {
                        if (newValue != null) {
                            java.util.Date date = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            switch (element.getType()) {
                                case BRIEFING -> getTournament().getSchedule().getBriefing().setDate(date);
                                case CLOSING_CEREMONY -> getTournament().getSchedule().getClosing().setDate(date);
                                default -> element.setDate(date);
                            }
                        } else {
                            element.setDate(null);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Schedule.ScheduleElement element = getTableRow().getItem();
                if (element != null) {
                    if (element.getDate() != null) {
                        datePicker.setValue(element.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } else {
                        datePicker.setValue(LocalDate.now());
                    }
                    setGraphic(datePicker);
                } else {
                    setGraphic(null);
                }
            }
        });

        getTournament().getScheduleElementsObs().addListener((ListChangeListener<? super Schedule.ScheduleElement>) e -> getScheduleTable().refresh());
        getScheduleTable().setItems(getTournament().getScheduleElementsObs());
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TableView<Schedule.ScheduleElement> getScheduleTable() {
        return scheduleTable;
    }

    public void setScheduleTable(TableView<Schedule.ScheduleElement> scheduleTable) {
        this.scheduleTable = scheduleTable;
    }

    public TableColumn<Schedule.ScheduleElement, String> getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(TableColumn<Schedule.ScheduleElement, String> scheduleName) {
        this.scheduleName = scheduleName;
    }

    public TableColumn<Schedule.ScheduleElement, Void> getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(TableColumn<Schedule.ScheduleElement, Void> scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
