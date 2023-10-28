package com.example.lolmanager.model;

import com.example.lolmanager.comparator.StartListComparator;

import java.io.IOException;
import java.util.ArrayList;

public class RoundRobinEngine implements Engine {
    public static int generatePairing(Tournament tournament) throws IOException, InterruptedException {
        PlayerList players = tournament.getPlayers();
        int roundsNo = players.size() % 2 == 0 ? players.size() - 1: players.size();
        int pairs = (roundsNo + 1) / 2;
        players.sort(new StartListComparator());
        int paired = 0;

        Player lastPlayer = players.size() % 2 == 0 ? players.get(players.size() - 1): players.getBye();
        Player.Color lastPlayerColor = Player.Color.BLACK;

        tournament.getRoundsObs().clear();
        ArrayList<Game> prevRound = new ArrayList<>();
        for (int i = 0; i < roundsNo; i++) {
            ArrayList<Game> round = new ArrayList<>();
            if (i == 0){
                for (int j = 0; j < pairs; j++) {
                    Player white = players.get(j);
                    Player black = j == 0 ? lastPlayer : players.get(roundsNo - j);
                    round.add(new Game(white, black));
                    paired++;
                }
            }else{
                for (int j = 0; j < pairs; j++) {
                    Player white;
                    Player black;
                    if (j == 0){
                        if (lastPlayerColor == Player.Color.WHITE){
                            white = prevRound.get(pairs - 1).getBlack();
                            black = lastPlayer;
                            lastPlayerColor = Player.Color.BLACK;
                        }else{
                            white = lastPlayer;
                            black = prevRound.get(pairs - 1).getBlack();
                            lastPlayerColor = Player.Color.WHITE;
                        }
                    }else{
                        black = prevRound.get(pairs - j).getWhite();
                        if (pairs - j - 1 == 0){
                            if (lastPlayerColor == Player.Color.BLACK){
                                white = prevRound.get(pairs - j - 1).getBlack();
                            }else{
                                white = prevRound.get(pairs - j - 1).getWhite();
                            }
                        }else{
                            white = prevRound.get(pairs - j - 1).getBlack();
                        }
                    }

                    paired++;
                    round.add(new Game(white, black));
                }
            }
            tournament.getRoundsObs().add(round);
            prevRound = round;
        }

        return paired;
    }

    public static boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing) {
        return false;
    }

    public static Tournament generateRandomTournament() {
        return null;
    }
}
