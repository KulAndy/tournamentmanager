package com.example.tournamentmanager.model;

import com.example.tournamentmanager.comparator.StartListComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.tournamentmanager.helper.DialogHelper.info;

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
                    Game game = new Game(white, black);
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

                    Game game = new Game(white, black);
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

        if (reversColors) {
            for (int i = 0; i < roundsNo; i++) {
                for (Game game : tournament.getRoundsObs().get(tournament.getRoundsObs().size() - 1 - i)) {
                    if (game.getWhite() != players.getBye() && game.getBlack() != players.getBye()) {
                        game.swapPlayers();
                    }
                }

            }
        }

        return paired;
    }

    public static Tournament generateRandomTournament() throws IOException, InterruptedException {
        Tournament tournament = new Tournament();
        tournament.setSystem(Tournament.TournamentSystem.ROUND_ROBIN);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int minRounds = 6;
        int maxRounds = 20;
        int minNameLength = 5;
        int maxNameLength = 25;
        Random random = new Random();
        Title[] titles = Title.values();
        int players = random.nextInt(maxRounds - minRounds + 1) + minRounds;
        for (int i = 0; i < players; i++) {
            Player player = new Player();
            player.setName("player" + (i + 1));
            player.setFideRating(players - i);
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

    public static void checkPairing(Tournament tournament, byte round) {
        StringBuilder builder = new StringBuilder();
        tournament.getPlayers().rehash();
        if (round == 0) {
            for (int i = 0; i < tournament.getRounds().size(); i++) {
                builder.append(checkRound(tournament, (byte) (i + 1)));
            }
        } else {
            builder.append(checkRound(tournament, round));
        }
        info(builder.toString().replaceAll("(?m)^\\\\s*\\\\r?\\\\n", ""));
    }

    private static String checkRound(Tournament tournament, byte round) {
        StringBuilder stringBuilder = new StringBuilder("round " + round);
        PlayerList players = tournament.getPlayers();
        int lastNo = players.size() % 2 == 0 ? players.size() : players.size() + 1;
        boolean ok = true;
        ArrayList<Player> checked = new ArrayList<>();
        for (Player player : players) {
            try {
                Player opponent = player.getOpponent(player.getRound(round - 1));
                if (!checked.contains(player) || !checked.contains(opponent)) {
                    Integer playerStartNo = players.getUuid2startNo().get(player.getPlayerid());
                    if (playerStartNo == null) {
                        playerStartNo = lastNo;
                    }
                    Integer opponentStartNo = players.getUuid2startNo().get(opponent.getPlayerid());
                    if (opponentStartNo == null) {
                        opponentStartNo = lastNo;
                    }
                    int diff = ((round - 1) % (lastNo - 1)) + 1 - playerStartNo;
                    int correctOpponent;
                    if (diff >= 0) {
                        correctOpponent = diff + 1;
                    } else {
                        correctOpponent = diff + lastNo;
                    }
                    if (correctOpponent == playerStartNo) {
                        correctOpponent = lastNo;
                    }
                    if (opponentStartNo != correctOpponent) {
                        stringBuilder.append("\n");
                        if (ok) {
                            stringBuilder.append("Checker pairings\tTournament pairings\n");
                        }
                        ok = false;
                        stringBuilder
                                .append("  %5d - %-5d".formatted(playerStartNo, correctOpponent))
                                .append("\t")
                                .append("  %5d - %-5d".formatted(playerStartNo, opponentStartNo));
                    }
                }
                checked.add(player);
                checked.add(opponent);
            } catch (Exception e) {
                System.out.println(round + " " + player.getRounds().size() + " " + player.getRounds());
            }
        }
        if (ok) {
            stringBuilder.append(" ok");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
