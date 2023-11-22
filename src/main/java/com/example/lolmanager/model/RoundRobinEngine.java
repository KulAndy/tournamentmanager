package com.example.lolmanager.model;

import com.example.lolmanager.comparator.StartListComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RoundRobinEngine implements Engine {
    public static int generatePairing(Tournament tournament, boolean reversColors) throws IOException, InterruptedException {
        PlayerList players = tournament.getPlayers();
        int roundsNo = players.size() % 2 == 0 ? players.size() - 1 : players.size();
        int pairs = (roundsNo + 1) / 2;
        players.sort(new StartListComparator());
        int paired = 0;

        Player lastPlayer = players.size() % 2 == 0 ? players.get(players.size() - 1) : players.getBye();
        Player.Color lastPlayerColor = Player.Color.BLACK;

        ArrayList<Game> prevRound = new ArrayList<>();
        for (int i = 0; i < roundsNo; i++) {
            ArrayList<Game> round = new ArrayList<>();
            if (i == 0) {
                for (int j = 0; j < pairs; j++) {
                    Player white = players.get(j);
                    Player black = j == 0 ? lastPlayer : players.get(roundsNo - j);
                    Game game;
                    if (reversColors && black != tournament.getPlayers().getBye()) {
                        game = new Game(black, white);
                    } else {
                        game = new Game(white, black);
                    }
                    round.add(game);
                    white.addRound(game);
                    if (black != tournament.getPlayers().getBye()) {
                        black.addRound(game);
                    }
                    paired++;
                }
            } else {
                for (int j = 0; j < pairs; j++) {
                    Player white;
                    Player black;
                    if (j == 0) {
                        if (lastPlayerColor == Player.Color.WHITE) {
                            white = prevRound.get(pairs - 1).getBlack();
                            black = lastPlayer;
                            lastPlayerColor = Player.Color.BLACK;
                        } else {
                            white = lastPlayer;
                            black = prevRound.get(pairs - 1).getBlack();
                            lastPlayerColor = Player.Color.WHITE;
                        }
                    } else {
                        black = prevRound.get(pairs - j).getWhite();
                        if (pairs - j - 1 == 0) {
                            if (lastPlayerColor == Player.Color.BLACK) {
                                white = prevRound.get(pairs - j - 1).getBlack();
                            } else {
                                white = prevRound.get(pairs - j - 1).getWhite();
                            }
                        } else {
                            white = prevRound.get(pairs - j - 1).getBlack();
                        }
                    }

                    Game game;
                    if (reversColors && black != tournament.getPlayers().getBye()) {
                        game = new Game(black, white);
                    } else {
                        game = new Game(white, black);
                    }
                    round.add(game);
                    white.addRound(game);
                    if (black != tournament.getPlayers().getBye()) {
                        black.addRound(game);
                    }
                    paired++;
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

    public static Tournament generateRandomTournament() throws IOException, InterruptedException {
        Tournament tournament = new Tournament();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int minRounds = 6;
        int maxRounds = 20;
        int minRating = 6;
        int maxRating = 20;
        int minNameLength = 5;
        int maxNameLength = 25;
        Random random = new Random();
        Title[] titles = Title.values();
        int players = random.nextInt(maxRounds - minRounds + 1) + minRounds;
        for (int i = 0; i < players; i++) {
            Player player = new Player();
            player.setName("player" + (i + 1));
            player.setFideRating(random.nextInt(maxRating - minRating + 1) + minRating);
            player.setTitle(titles[random.nextInt(titles.length)]);
            tournament.getPlayersObs().add(player);
        }

        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 0; i < random.nextInt(maxNameLength - minNameLength + 1) + minNameLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            nameBuilder.append(randomChar);
        }

        StringBuilder placeBuilder = new StringBuilder();

        for (int i = 0; i < random.nextInt(maxNameLength - minNameLength + 1) + minNameLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            placeBuilder.append(randomChar);
        }
        tournament.setName(nameBuilder.toString());
        tournament.setPlace(placeBuilder.toString());
        generatePairing(tournament, false);

        return tournament;
    }
}
