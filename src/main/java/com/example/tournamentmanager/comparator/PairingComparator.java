package com.example.tournamentmanager.comparator;

import com.example.tournamentmanager.model.Game;
import com.example.tournamentmanager.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class PairingComparator implements Comparator<Game>, Serializable {
    private transient ObservableList<Player> playersObs;

    public PairingComparator() {
        this(FXCollections.observableArrayList());
    }

    public PairingComparator(ObservableList<Player> players) {
        setPlayersObs(players);
    }

    @Override
    public int compare(Game o1, Game o2) {
        Player white1 = o1.getWhite();
        Player white2 = o2.getWhite();
        Player black1 = o1.getBlack();
        Player black2 = o2.getBlack();

        if (white1 == null || black1 == null) {
            return -1;
        } else if (white2 == null || black2 == null) {
            return 1;
        } else if (
                Objects.equals(black1.getName(), "bye") && !(Objects.equals(black2.getName(), "bye"))
        ) {
            if (Objects.equals(black2.getName(), "halfbye") || Objects.equals(black2.getName(), "unpaired")) {
                return -1;
            } else {
                return 1;
            }
        } else if (Objects.equals(black1.getName(), "halfbye") && !(Objects.equals(black2.getName(), "halfbye"))) {
            if (Objects.equals(black2.getName(), "unpaired")) {
                return -1;
            } else {
                return 1;
            }
        } else if (Objects.equals(black1.getName(), "unpaired") && !(Objects.equals(black2.getName(), "unpaired"))) {
            return 1;
        } else if (
                Objects.equals(black2.getName(), "bye") && !(Objects.equals(black1.getName(), "bye"))
        ) {
            if (Objects.equals(black1.getName(), "halfbye") || Objects.equals(black1.getName(), "unpaired")) {
                return 1;
            } else {
                return -1;
            }
        } else if (Objects.equals(black2.getName(), "halfbye") && !(Objects.equals(black1.getName(), "halfbye"))) {
            if (Objects.equals(black1.getName(), "unpaired")) {
                return 1;
            } else {
                return -1;
            }
        } else if (Objects.equals(black2.getName(), "unpaired") && !(Objects.equals(black1.getName(), "unpaired"))) {
            return -1;
        } else {
            int roundNo = Integer.min(white1.getRounds().indexOf(o1), white2.getRounds().indexOf(o2));
            float maxResult1 = Float.max(
                    white1.getPointInRound(roundNo),
                    black1.getPointInRound(roundNo)
            );
            float maxResult2 = Float.max(
                    white2.getPointInRound(roundNo),
                    black2.getPointInRound(roundNo)
            );
            if (maxResult1 != maxResult2) {
                return -Float.compare(maxResult1, maxResult2);
            } else {
                float sumResult1 = white1.getPointInRound(roundNo) + black1.getPointInRound(roundNo);
                float sumResult2 = white2.getPointInRound(roundNo) + black2.getPointInRound(roundNo);
                if (sumResult1 != sumResult2) {
                    return -Float.compare(sumResult1, sumResult2);
                } else {
                    int minStr1 = Integer.min(
                            playersObs.contains(white1) ? playersObs.indexOf(white1) : Integer.MAX_VALUE,
                            playersObs.contains(black1) ? playersObs.indexOf(black1) : Integer.MAX_VALUE
                    );
                    int minStr2 = Integer.min(
                            playersObs.contains(white2) ? playersObs.indexOf(white2) : Integer.MAX_VALUE,
                            playersObs.contains(black2) ? playersObs.indexOf(black2) : Integer.MAX_VALUE
                    );
                    if (minStr1 != minStr2) {
                        return Integer.compare(minStr1, minStr2);
                    } else {
                        int higherPlayer1 = black1.getFideRating() > white1.getFideRating() ? playersObs.indexOf(black1) : playersObs.indexOf(white1);
                        int higherPlayer2 = black2.getFideRating() > white2.getFideRating() ? playersObs.indexOf(black2) : playersObs.indexOf(white2);


                        return Integer.compare(higherPlayer1, higherPlayer2);
                    }
                }

            }
        }
    }

    public ObservableList<Player> getPlayersObs() {
        return playersObs;
    }

    public void setPlayersObs(ObservableList<Player> playersObs) {
        this.playersObs = playersObs;
    }

}
