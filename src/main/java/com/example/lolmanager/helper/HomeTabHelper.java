package com.example.lolmanager.helper;

import com.example.lolmanager.helper.home.BasicInfoHelper;
import com.example.lolmanager.helper.home.RtgOptHelper;
import com.example.lolmanager.helper.home.ScheduleHelper;
import com.example.lolmanager.helper.home.TiebreakHelper;
import com.example.lolmanager.model.Schedule;
import com.example.lolmanager.model.Title;
import com.example.lolmanager.model.Tournament;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Method;

import static com.example.lolmanager.helper.GeneralHelper.getSetter;
import static com.example.lolmanager.helper.GeneralHelper.logTournamentValue;

public class HomeTabHelper {
    private BasicInfoHelper basicInfoHelper;
    private TiebreakHelper tiebreakHelper;
    private RtgOptHelper rtgOptHelper;
    private ScheduleHelper scheduleHelper;

    public HomeTabHelper(
            Tournament tournament, TextField tourName, DatePicker tourStartDate, DatePicker tourEndDate, TextField tourPlace, TextField tourGameTime,
            TextField tourIncrement, TextField tourControlMove, TextField tourControlAddition, ComboBox<Tournament.Type> tourType, CheckBox tourRtPZSzach, CheckBox tourRtFIDE,
            TextField tourNoRounds, ComboBox<Tournament.TournamentSystem> tourSystem, TextField tourArbiter, TextField tourOrganizer, TextField tourEmail,
            CheckBox tourFIDEMode, GridPane tiebreakPane, ComboBox<Tournament.Tiebreak.TbMethod> tourTB1, ComboBox<Tournament.Tiebreak.TbMethod> tourTB2,
            ComboBox<Tournament.Tiebreak.TbMethod> tourTB3, ComboBox<Tournament.Tiebreak.TbMethod> tourTB4, ComboBox<Tournament.Tiebreak.TbMethod> tourTB5,
            GridPane pointsPane, TextField pointsWin, TextField pointsDraw, TextField pointsLose, TextField pointsForfeitWin, TextField pointsForfeitLose,
            TextField pointsBye, TextField pointsHalfBye,
            TextField minInitGames,
            TextField ratingFloor,
            CheckBox PZSzach43Cb,
            CheckBox PZSzach44Cb,
            CheckBox PZSzach46Cb,
            CheckBox PZSzach47Cb,
            ComboBox<Title> maxTitle,
            CheckBox twoOtherFeds,
            TextField minTitleGames,
            TableView<Schedule.ScheduleElement> scheduleTable,
            TableColumn<Schedule.ScheduleElement, String> scheduleName,
            TableColumn<Schedule.ScheduleElement, Void> scheduleDate
    ) {
        setBasicInfoHelper(
                new BasicInfoHelper(
                        tournament, tourName, tourStartDate, tourEndDate, tourPlace, tourGameTime,
                        tourIncrement, tourControlMove, tourControlAddition, tourType, tourRtPZSzach, tourRtFIDE,
                        tourNoRounds, tourSystem, tourArbiter, tourOrganizer, tourEmail
                )
        );
        setTiebreakHelper(
                new TiebreakHelper(
                        tournament,
                        tourFIDEMode, tiebreakPane, tourTB1, tourTB2, tourTB3, tourTB4, tourTB5,
                        pointsPane, pointsWin, pointsDraw, pointsLose, pointsForfeitWin, pointsForfeitLose,
                        pointsBye, pointsHalfBye
                )
        );
        setRtgOptHelper(
                new RtgOptHelper(tournament,
                        minInitGames, ratingFloor, PZSzach43Cb, PZSzach44Cb, PZSzach46Cb, PZSzach47Cb, maxTitle, twoOtherFeds, minTitleGames
                )
        );

        setScheduleHelper(new ScheduleHelper(
                tournament, scheduleTable, scheduleName, scheduleDate
        ));
        tourSystem.valueProperty().addListener(
                (ObservableValue<? extends Tournament.TournamentSystem> observable, Tournament.TournamentSystem oldValue, Tournament.TournamentSystem newValue) -> {
                    String attr = "system";
                    if (newValue != null) {
                        try {
                            Method setter = getSetter(tournament, attr, Tournament.TournamentSystem.class);
                            setter.invoke(tournament, newValue);
                            if (tourFIDEMode.isSelected()) {
                                getTiebreakHelper().setupFIDEMode();
                            }
                            logTournamentValue(tournament, attr);
                        } catch (Exception ignored) {
                        }

                    }

                });


    }

    public BasicInfoHelper getBasicInfoHelper() {
        return basicInfoHelper;
    }

    public void setBasicInfoHelper(BasicInfoHelper basicInfoHelper) {
        this.basicInfoHelper = basicInfoHelper;
    }

    public TiebreakHelper getTiebreakHelper() {
        return tiebreakHelper;
    }

    public void setTiebreakHelper(TiebreakHelper tiebreakHelper) {
        this.tiebreakHelper = tiebreakHelper;
    }

    public RtgOptHelper getRtgOptHelper() {
        return rtgOptHelper;
    }

    public void setRtgOptHelper(RtgOptHelper rtgOptHelper) {
        this.rtgOptHelper = rtgOptHelper;
    }
    public ScheduleHelper getScheduleHelper() {
        return scheduleHelper;
    }

    public void setScheduleHelper(ScheduleHelper scheduleHelper) {
        this.scheduleHelper = scheduleHelper;
    }
}
