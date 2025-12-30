package com.example.tournamentmanager.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class CupEngine implements Engine {

    public static int generatePairing(Tournament tournament, boolean reversColors) throws UnfinishedRound {
        ArrayList<ArrayList<Game>> rounds = tournament.getRounds();
        int roundNo = rounds.size() + 1;
        ArrayList<Game> round = new ArrayList<>();
        int paired = 0;
        if (roundNo > 1) {
            ArrayList<Game> prevRound = rounds.getLast();
            for (int i = 0; i < prevRound.size(); i += 2) {
                Player winner1 = prevRound.get(i).getWinner();
                Player loser1 = prevRound.get(i).getLoser();
                Player winner2 = prevRound.get(i + 1).getWinner();
                Player loser2 = prevRound.get(i + 1).getLoser();
                if (winner1 == null || loser1 == null || winner2 == null || loser2 == null) {
                    throw new UnfinishedRound();
                }
                Game game;
                if (winner1.getPlayerid().toString().equals(tournament.getPlayers().getBye().getPlayerid().toString())) {
                    game = new Game(winner2, winner1, Result.WIN, Result.LOSE, true);
                } else if (winner2.getPlayerid().toString().equals(tournament.getPlayers().getBye().getPlayerid().toString())) {
                    game = new Game(winner1, winner2, Result.WIN, Result.LOSE, true);
                } else {
                    int startNo1 = tournament.getPlayers().indexOf(winner1);
                    int startNo2 = tournament.getPlayers().indexOf(winner2);
                    int colorPreference1 = winner1.getColorPreference();
                    int colorPreference2 = winner2.getColorPreference();
                    if (startNo1 < startNo2 && Math.abs(colorPreference1) < Math.abs(colorPreference2)) {
                        if (colorPreference2 > 0) {
                            game = new Game(winner1, winner2);
                        } else {
                            game = new Game(winner2, winner1);
                        }
                    } else {
                        if (colorPreference1 > 0) {
                            game = new Game(winner2, winner1);
                        } else {
                            game = new Game(winner1, winner2);
                        }
                    }
                }
                round.add(game);
                winner1.addRound(game);
                winner2.addRound(game);
                paired++;
            }
        } else {
            round = createPairings(tournament.getPlayers());
            Player.Color color = getColor(tournament);
            for (int j = 0; j < round.size(); j++) {
                if (
                        color == Player.Color.BLACK && j % 2 == 0
                                && !round.get(j).getBlack().getPlayerid().toString().equals(tournament.getPlayers().getBye().getPlayerid().toString())
                ) {
                    round.get(j).swapPlayers();
                } else if (
                        color == Player.Color.WHITE && j % 2 == 1
                                && !round.get(j).getBlack().getPlayerid().toString().equals(tournament.getPlayers().getBye().getPlayerid().toString())
                ) {
                    round.get(j).swapPlayers();
                }
            }
            paired = round.size();
        }
        tournament.getRoundsObs().add(round);
        return paired;
    }

    private static Player.Color getColor(Tournament tournament) {
        Player.Color color;
        if (tournament.getFirstColor() == null) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[1];
            secureRandom.nextBytes(randomBytes);
            int randomValue = Math.abs(randomBytes[0] % 2);
            if (randomValue == 0) {
                color = Player.Color.WHITE;
            } else {
                color = Player.Color.BLACK;
            }
        } else {
            color = tournament.getFirstColor();
        }
        return color;
    }

    private static ArrayList<Game> createPairings(PlayerList players) {
        PlayerList playersCopy = new PlayerList();
        playersCopy.addAll(players);
        playersCopy.setBye(players.getBye());
        playersCopy.setUnpaired(players.getUnpaired());
        ArrayList<Game> pairing = new ArrayList<>();
        if (playersCopy.isEmpty()) {
            return pairing;
        } else if (playersCopy.size() == 1) {
            pairing.add(new Game(playersCopy.getFirst(), players.getBye(), Result.WIN, Result.LOSE, true));
            return pairing;
        } else if (playersCopy.size() == 2) {
            pairing.add(new Game(playersCopy.get(0), playersCopy.get(1)));
            return pairing;
        } else {
            PlayerList groupA = new PlayerList();
            PlayerList groupB = new PlayerList();

            groupA.add(playersCopy.removeFirst());
            groupA.add(playersCopy.removeLast());
            byte flag = 1; //0 - A, 1 - B, 2 - B, 3 - A
            while (!playersCopy.isEmpty()) {
                if (flag == 0 || flag == 3) {
                    groupA.add(playersCopy.removeFirst());
                    if (!playersCopy.isEmpty()) {
                        if (groupA.size() - 1 == groupB.size() && playersCopy.size() == 1) {
                            groupB.add(playersCopy.removeFirst());
                        } else {
                            groupA.add(playersCopy.removeLast());
                        }
                    }
                } else {
                    groupB.add(playersCopy.removeFirst());
                    if (!playersCopy.isEmpty()) {
                        if (groupA.size() == groupB.size() - 1 && playersCopy.size() == 1) {
                            groupA.add(playersCopy.removeFirst());
                        } else {
                            groupB.add(playersCopy.removeLast());
                        }
                    }


                }
                flag++;
                flag %= 4;
            }

            Collections.sort(groupA, Comparator.comparingInt(players::indexOf));
            Collections.sort(groupB, Comparator.comparingInt(players::indexOf));

            ArrayList<Game> pairingA = createPairings(groupA);
            ArrayList<Game> pairingB = createPairings(groupB);

            for (int i = pairingA.size() - 1; i >= 0 && pairingA.size() < pairingB.size(); i--) {
                Game game = pairingA.get(i);
                Player player2 = game.getBlack();
                player2.getRounds().remove(game);
                pairingA.add(new Game(player2, players.getBye(), Result.WIN, Result.LOSE, true));
                game.setBlack(players.getBye());
                game.setWhiteResult(Result.WIN);
                game.setBlackResult(Result.LOSE);
                game.setForfeit(true);
            }

            for (int i = pairingB.size() - 1; i >= 0 && pairingA.size() > pairingB.size(); i--) {
                Game game = pairingB.get(i);
                Player player2 = game.getBlack();
                player2.getRounds().remove(game);
                pairingB.add(new Game(player2, players.getBye(), Result.WIN, Result.LOSE, true));
                game.setBlack(players.getBye());
                game.setWhiteResult(Result.WIN);
                game.setBlackResult(Result.LOSE);
                game.setForfeit(true);
            }

            Collections.reverse(pairingB);
            pairing.addAll(pairingA);
            pairing.addAll(pairingB);

            return pairing;
        }
    }

    public static void checkPairing(Tournament tournament, byte round) {
    }

    public static Tournament generateRandomTournament() {
        Tournament tournament = new Tournament();
        tournament.setSystem(Tournament.TournamentSystem.CUP);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int minPlayers = 4;
        int maxPlayers = 32;
        int minNameLength = 5;
        int maxNameLength = 25;
        Random random = new Random();
        Title[] titles = Title.values();
        int players = random.nextInt(maxPlayers - minPlayers + 1) + minPlayers;
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
        try {
            int roundsNo = (byte) Math.ceil(
                    Math.log(tournament.getPlayers().size()) / Math.log(2)
            );
            for (int i = 0; i < roundsNo; i++) {
                int paired = generatePairing(tournament, false);
                for (int j = 0; j < paired; j++) {
                    if (tournament.getRoundsObs().get(i).get(j).getBlack() == tournament.getPlayers().getBye()) {
                        continue;
                    }
                    SecureRandom secureRandom = new SecureRandom();
                    byte[] randomBytes = new byte[1];

                    secureRandom.nextBytes(randomBytes);
                    int randomValue = Math.abs(randomBytes[0] % 2);
                    if (randomValue == 0) {
                        tournament.getRoundsObs().get(i).get(j).setWhiteResult(Result.WIN);
                        tournament.getRoundsObs().get(i).get(j).setBlackResult(Result.LOSE);
                        tournament.getRoundsObs().get(i).get(j).setForfeit(false);
                    } else {
                        tournament.getRoundsObs().get(i).get(j).setWhiteResult(Result.LOSE);
                        tournament.getRoundsObs().get(i).get(j).setBlackResult(Result.WIN);
                        tournament.getRoundsObs().get(i).get(j).setForfeit(false);
                    }
                }
            }

            return tournament;
        } catch (UnfinishedRound e) {
            return tournament;
        }
    }

    public static class UnfinishedRound extends Exception {
        public UnfinishedRound() {
            super("Previous round has not been finished");
        }
    }
}
