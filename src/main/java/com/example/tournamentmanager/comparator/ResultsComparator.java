package com.example.tournamentmanager.comparator;

import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Tournament;

import java.io.Serializable;
import java.util.Comparator;

public class ResultsComparator implements Comparator<Player>, Serializable {

    private Tournament.Tiebreak.TbMethod criteria1;
    private Tournament.Tiebreak.TbMethod criteria2;
    private Tournament.Tiebreak.TbMethod criteria3;
    private Tournament.Tiebreak.TbMethod criteria4;
    private Tournament.Tiebreak.TbMethod criteria5;

    public ResultsComparator() {
        this(
                Tournament.Tiebreak.TbMethod.POINTS,
                Tournament.Tiebreak.TbMethod.BUCHOLZ_CUT1,
                Tournament.Tiebreak.TbMethod.BUCHOLZ,
                Tournament.Tiebreak.TbMethod.SONNEN_BERGER,
                Tournament.Tiebreak.TbMethod.PROGRESS
        );
    }

    public ResultsComparator(Tournament.Tiebreak tiebreak) {
        this(
                tiebreak.getTiebreak1(),
                tiebreak.getTiebreak2(),
                tiebreak.getTiebreak3(),
                tiebreak.getTiebreak4(),
                tiebreak.getTiebreak5()
        );
    }

    public ResultsComparator(
            Tournament.Tiebreak.TbMethod criteria1,
            Tournament.Tiebreak.TbMethod criteria2,
            Tournament.Tiebreak.TbMethod criteria3,
            Tournament.Tiebreak.TbMethod criteria4,
            Tournament.Tiebreak.TbMethod criteria5
    ) {
        setCriteria1(criteria1);
        setCriteria2(criteria2);
        setCriteria3(criteria3);
        setCriteria4(criteria4);
        setCriteria5(criteria5);
    }

    @Override
    public int compare(Player player1, Player player2) {
        int result;
        result = compare(player1, player2, getCriteria1());
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, getCriteria2());
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, getCriteria3());
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, getCriteria4());
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, getCriteria5());
        return -result;
    }

    public int compare(Player player1, Player player2, Tournament.Tiebreak.TbMethod criteria) {
        switch (criteria) {
            case POINTS -> {
                return Float.compare(player1.getPoints(), player2.getPoints());
            }
            case DUEL -> {
                return Float.compare(player1.getDuel(), player2.getDuel());
            }
            case WINS -> {
                return Integer.compare(player1.getWinsNumber(), player2.getWinsNumber());
            }
            case GAMES_WITH_BLACK -> {
                return Integer.compare(player1.getGamesPlayedWithBlack(), player2.getGamesPlayedWithBlack());
            }
            case WINS_WITH_BLACK -> {
                return Integer.compare(player1.getWinsWithBlackNumber(), player2.getWinsWithBlackNumber());
            }
            case BUCHOLZ -> {
                return Float.compare(player1.getBucholz(), player2.getBucholz());
            }
            case BUCHOLZ_CUT1 -> {
                return Float.compare(player1.getBucholzCut1(), player2.getBucholzCut1());
            }
            case SONNEN_BERGER -> {
                return Float.compare(player1.getBerger(), player2.getBerger());
            }
            case PROGRESS -> {
                return Float.compare(player1.getProgress(), player2.getProgress());
            }
            case KOYA -> {
                return Float.compare(player1.getKoya(), player2.getKoya());
            }
            case RATING_PERFORMENCE_PZSZACH -> {
                return Integer.compare(player1.getRatingPerformancePZSzach(), player2.getRatingPerformancePZSzach());
            }
            case AVERAGE_OPPONENTS_RATING -> {
                return Integer.compare(player1.getAverageFideRating(), player2.getAverageFideRating());
            }
            case AVERAGE_OPPONENTS_LOCAL_RATING -> {
                return Integer.compare(player1.getAverageRatingPZSzach(), player2.getAverageRatingPZSzach());
            }
            case RATING_PERFORMENCE_FIDE -> {
                return Float.compare(player1.getRatingPerformanceFide(), player2.getRatingPerformanceFide());
            }
        }
        return 0;
    }

    public Tournament.Tiebreak.TbMethod getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(Tournament.Tiebreak.TbMethod criteria1) {
        this.criteria1 = criteria1;
    }

    public Tournament.Tiebreak.TbMethod getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(Tournament.Tiebreak.TbMethod criteria2) {
        this.criteria2 = criteria2;
    }

    public Tournament.Tiebreak.TbMethod getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(Tournament.Tiebreak.TbMethod criteria3) {
        this.criteria3 = criteria3;
    }

    public Tournament.Tiebreak.TbMethod getCriteria4() {
        return criteria4;
    }

    public void setCriteria4(Tournament.Tiebreak.TbMethod criteria4) {
        this.criteria4 = criteria4;
    }

    public Tournament.Tiebreak.TbMethod getCriteria5() {
        return criteria5;
    }

    public void setCriteria5(Tournament.Tiebreak.TbMethod criteria5) {
        this.criteria5 = criteria5;
    }
}
