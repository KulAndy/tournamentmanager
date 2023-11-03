package com.example.lolmanager.operation;

import com.example.lolmanager.MainController;
import com.example.lolmanager.model.Tournament;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TournamentOperation {
    public static void loadTournament(Tournament tournament, MainController controller) {
        controller.getTourName().setText(tournament.getName());
        LocalDate localStartDate;
        LocalDate localEndDate;
        if (tournament.getStartDate() == null) {
            localStartDate = LocalDate.now();
        } else {
            localStartDate = tournament.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (tournament.getEndDate() == null) {
            localEndDate = LocalDate.now();
        } else {
            localEndDate = tournament.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        controller.getTourStartDate().setValue(localStartDate);
        controller.getTournament().setStartDate(Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        controller.getTourEndDate().setValue(localEndDate);
        controller.getTournament().setEndDate(Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        controller.getTourPlace().setText(tournament.getPlace());
        controller.getTourGameTime().setText(String.valueOf(tournament.getGameTime()));
        controller.getTourIncrement().setText(String.valueOf(tournament.getIncrement()));
        controller.getTourControlMove().setText(String.valueOf(tournament.getControlMove()));
        controller.getTourControlAddition().setText(String.valueOf(tournament.getControlAddition()));
        controller.getTourType().setValue(tournament.getType());
        controller.getTourRtPZSzach().setSelected(tournament.getRating().getPZSzachRated());
        controller.getTourRtFIDE().setSelected(tournament.getRating().getFIDERated());
        controller.getTourNoRounds().setText(String.valueOf(tournament.getRoundsNumber()));
        controller.getTourSystem().setValue(tournament.getSystem());
        controller.getTourArbiter().setText(tournament.getArbiter());
        controller.getTourOrganizer().setText(tournament.getOrganizer());
        controller.getTourEmail().setText(tournament.getEmail());
        controller.getTourTB1().setValue(tournament.getTiebreak().getTiebreak1());
        controller.getTourTB2().setValue(tournament.getTiebreak().getTiebreak2());
        controller.getTourTB3().setValue(tournament.getTiebreak().getTiebreak3());
        controller.getTourTB4().setValue(tournament.getTiebreak().getTiebreak4());
        controller.getTourTB5().setValue(tournament.getTiebreak().getTiebreak5());
        controller.getPointsWin().setText(String.valueOf(tournament.getTiebreak().getWinPoints()));
        controller.getPointsDraw().setText(String.valueOf(tournament.getTiebreak().getDrawPoints()));
        controller.getPointsLose().setText(String.valueOf(tournament.getTiebreak().getLosePoints()));
        controller.getPointsForfeitWin().setText(String.valueOf(tournament.getTiebreak().getForfeitWinPoints()));
        controller.getPointsForfeitLose().setText(String.valueOf(tournament.getTiebreak().getForfeitLosePoints()));
        controller.getPointsBye().setText(String.valueOf(tournament.getTiebreak().getByePoints()));
        controller.getPointsHalfBye().setText(String.valueOf(tournament.getTiebreak().getHalfByePoints()));
        controller.getMinInitGames().setText(String.valueOf(tournament.getRating().getMinInitGames()));
        controller.getRatingFloor().setText(String.valueOf(tournament.getRating().getRatingFloor()));
        controller.getPZSzach43Cb().setSelected(tournament.getRating().getPZSzach43());
        controller.getPZSzach44Cb().setSelected(tournament.getRating().getPZSzach44());
        controller.getPZSzach46Cb().setSelected(tournament.getRating().getPZSzach46());
        controller.getPZSzach47Cb().setSelected(tournament.getRating().getPZSzach47());
        controller.getMaxTitle().setValue(tournament.getRating().getMaxTitle());
        controller.getTwoOtherFeds().setSelected(tournament.getRating().getTwoOtherFederations());
        controller.getMinTitleGames().setText(String.valueOf(tournament.getRating().getMinTitleGames()));
        controller.getTournament().getPlayersObs().clear();
        controller.getTournament().getPlayersObs().addAll(tournament.getPlayers());
        controller.getTournament().getPlayers().setBye(tournament.getPlayers().getBye());
        controller.getTournament().getPlayers().setHalfbye(tournament.getPlayers().getHalfbye());
        controller.getTournament().getPlayers().setUnpaired(tournament.getPlayers().getUnpaired());
        controller.getTournament().getRoundsObs().clear();
        controller.getTournament().getRoundsObs().addAll(tournament.getRounds());
        controller.getTournament().getWithdrawsObs().clear();
        controller.getTournament().getWithdrawsObs().addAll(tournament.getWithdraws());
        controller.getPlayersHelper().getPlayersSortHelper().getCriteria1().setValue(tournament.getPlayers().getComparator().getCriteria1());
        controller.getPlayersHelper().getPlayersSortHelper().getCriteria2().setValue(tournament.getPlayers().getComparator().getCriteria2());
        controller.getPlayersHelper().getPlayersSortHelper().getCriteria3().setValue(tournament.getPlayers().getComparator().getCriteria3());
        controller.getPlayersHelper().getPlayersSortHelper().getCriteria4().setValue(tournament.getPlayers().getComparator().getCriteria4());
        controller.getPlayersHelper().getPlayersSortHelper().getCriteria5().setValue(tournament.getPlayers().getComparator().getCriteria5());

    }
}
