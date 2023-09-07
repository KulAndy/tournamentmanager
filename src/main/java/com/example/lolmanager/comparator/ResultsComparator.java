package com.example.lolmanager.comparator;

import com.example.lolmanager.model.Game;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Tournament;

import java.io.Serializable;
import java.util.ArrayList;
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
        int result = 0;
        result = compare(player1, player2, criteria1);
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, criteria2);
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, criteria3);
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, criteria4);
        if (result != 0) {
            return -result;
        }
        result = compare(player1, player2, criteria5);
        return -result;
    }

    public int compare(Player player1, Player player2, Tournament.Tiebreak.TbMethod criteria) {
        switch (criteria) {
            case POINTS -> {
                return Float.compare(player1.getPoints(), player2.getPoints());
            }
            case DUEL -> {
                ArrayList<Player> opponents = player1.getOpponents();
                if (opponents.contains(player2)) {
                    ArrayList<Game> rounds = player1.getRounds();
                    for (Game round : rounds) {
                        if (round.getWhite() == player2) {
                            if (round.getPointsForWhite() == 1.0) {
                                return -1;
                            } else if (round.getPointsForBlack() == 0.0) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } else if (round.getBlack() == player2) {
                            if (round.getPointsForWhite() == 1.0) {
                                return 1;
                            } else if (round.getPointsForBlack() == 0.0) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
                return 0;
            }
            case WONS -> {
                return Integer.compare(player1.getWonsNumber(), player2.getWonsNumber());
            }
            case GAMES_WITH_BLACK -> {
                return Integer.compare(player1.getGamesPlayedWithBlack(), player2.getGamesPlayedWithBlack());
            }
            case WONS_WITH_BLACK -> {
                return Integer.compare(player1.getWonsWithBlackNumber(), player2.getWonsWithBlackNumber());
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
                return Integer.compare(player1.getRatingPerformance(), player2.getRatingPerformance());
            }
            case AVERAGE_OPPONENTS_RATING -> {
                return Integer.compare(player1.getAverageFideRating(), player2.getAverageFideRating());
            }
            case AVERAGE_OPPONENTS_LOCAL_RATING -> {
                return Integer.compare(player1.getAverageRating(), player2.getAverageRating());
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
