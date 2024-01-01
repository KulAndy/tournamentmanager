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
    public int compare(Game game1, Game game2) {
        Player white1 = game1.getWhite();
        Player white2 = game2.getWhite();
        Player black1 = game1.getBlack();
        Player black2 = game2.getBlack();

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
            return compareByPointsAndRating(game1, game2);
        }
    }

    private int compareByPointsAndRating(Game game1, Game game2) {
        Player white1 = game1.getWhite();
        Player black1 = game1.getBlack();
        Player white2 = game2.getWhite();
        Player black3 = game2.getBlack();
        int roundNo1 = Integer.min(white1.getRounds().indexOf(game1), black1.getRounds().indexOf(game1));
        int roundNo2 = Integer.min(white2.getRounds().indexOf(game2), white2.getRounds().indexOf(game2));

        float maxPoints1 = Math.max(white1.getPointInRound(roundNo1), black1.getPointInRound(roundNo1));
        float maxPoints2 = Math.max(white2.getPointInRound(roundNo2), black3.getPointInRound(roundNo2));

        if (maxPoints1 != maxPoints2) {
            return Float.compare(maxPoints2, maxPoints1);
        }

        float sumPoints1 = white1.getPointInRound(roundNo1) + black1.getPointInRound(roundNo1);
        float sumPoints2 = white2.getPointInRound(roundNo2) + black3.getPointInRound(roundNo2);

        if (sumPoints1 != sumPoints2) {
            return Float.compare(sumPoints2, sumPoints1);
        }

        int maxRating1 = Math.max(white1.getFideRating(), black1.getFideRating());
        int maxRating2 = Math.max(white2.getFideRating(), black3.getFideRating());

        return Integer.compare(maxRating2, maxRating1);
    }

    public ObservableList<Player> getPlayersObs() {
        return playersObs;
    }

    public void setPlayersObs(ObservableList<Player> playersObs) {
        this.playersObs = playersObs;
    }

}
