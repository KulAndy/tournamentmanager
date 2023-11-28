package com.example.lolmanager.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.lolmanager.operation.FIDEOperation.saveTrfReport;
import static com.example.lolmanager.operation.FIDEOperation.trfReport;

public class JavafoWrapper implements Engine {
    private static final String javaPath = "java";
    private static final String javafoPath = "javafo.jar";
    private static final String outputFilePath = "pairing.txt";
    private static final String reportFilePath = "report.txt";

    public static int generatePairing(Tournament tournament, boolean reversColors) throws IOException, InterruptedException {
        File outputFile = new File(outputFilePath);
        File reportFile = new File(reportFilePath);
        saveTrfReport(trfReport(tournament), reportFile);
        List<String> command = new ArrayList<>();
        command.add(javaPath);
        command.add("-jar");
        command.add(javafoPath);
        command.add(reportFilePath);
        command.add("-p");
        command.add(outputFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            //ignore numer of pairings
            line = reader.readLine();
            ArrayList<Game> round = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] ids = line.trim().split(" ");
                int whiteId = Integer.parseInt(ids[0]);
                int blackId = Integer.parseInt(ids[1]);
                Player white = tournament.getPlayers().get(whiteId - 1);
                Player black = blackId == 0 ? tournament.getPlayers().getBye() : tournament.getPlayers().get(blackId - 1);
                Game game;
                if (reversColors && blackId != 0) {
                    game = new Game(black, white);
                } else {
                    game = new Game(white, black);
                }
                round.add(game);
                white.addRound(game);
                if (blackId != 0) {
                    black.addRound(game);
                }
            }
            for (Withdraw withdraw : tournament.getWithdraws()) {
                Player player = withdraw.getPlayer();
                switch (withdraw.getType()) {
                    case ROUND -> {
                        if (withdraw.getRoundNo() == tournament.getRounds().size() + 1) {
                            round.add(new Game(player, tournament.getPlayers().getUnpaired(), Result.LOSE, Result.WIN, true));
                        }
                    }
                    case HALFBYE -> {
                        if (withdraw.getRoundNo() == tournament.getRounds().size() + 1) {
                            round.add(new Game(player, tournament.getPlayers().getHalfbye(), Result.DRAW, Result.DRAW, true));
                        }
                    }
                    default ->
                            round.add(new Game(player, tournament.getPlayers().getUnpaired(), Result.LOSE, Result.WIN, true));

                }
            }
            tournament.getRoundsObs().add(round);
            return round.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputFile.delete();
        } catch (Exception ignored) {
        }
        try {
            reportFile.delete();
        } catch (Exception ignored) {
        }

        return 0;
    }

    public static boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing) {
        return false;
    }

    public static Tournament generateRandomTournament() throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add(javaPath);
        command.add("-jar");
        command.add(javafoPath);
        command.add("-g");
        command.add("-o");
        command.add(reportFilePath);
        File reportFile = new File(reportFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
        return new Tournament(new TrfTournament(reportFile));
    }
}
