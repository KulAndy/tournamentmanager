package com.example.tournamentmanager.comparator;

import com.example.tournamentmanager.model.Player;

import java.io.Serializable;
import java.util.Comparator;

public class ResultsComparator implements Comparator<Player>, Serializable {

    @Override
    public int compare(Player player1, Player player2) {
        int result;
        result = Float.compare(player1.getTb1(), player2.getTb1());
        if (result != 0) {
            return -result;
        }
        result = Float.compare(player1.getTb2(), player2.getTb2());
        if (result != 0) {
            return -result;
        }
        result = Float.compare(player1.getTb3(), player2.getTb3());
        if (result != 0) {
            return -result;
        }
        result = Float.compare(player1.getTb4(), player2.getTb4());
        if (result != 0) {
            return -result;
        }
        return Float.compare(player1.getTb5(), player2.getTb5());
    }
}
