package com.example.lolmanager.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.lolmanager.operation.FIDEOperation.saveTrfReport;
import static com.example.lolmanager.operation.FIDEOperation.trfReport;

public class JavafoWrapper implements Engine {
    private static String javaPath = "java.exe";
    private static String javafoPath = "./javafo.jar";
    private static File outputFile = new File("./pairing.txt");

    public ArrayList<Game> generatePairing(Tournament tournament) throws IOException, InterruptedException {
        File report = new File("./report.txt");
        saveTrfReport(trfReport(tournament), report);
        List<String> command = new ArrayList<>();
        command.add(javaPath);
        command.add("-jar");
        command.add(javafoPath);
        command.add(report.getAbsolutePath());
        command.add("-p");
        command.add(outputFile.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile)) ) {
            String line;
            //ignore numer of pairings
            line = reader.readLine();
            ArrayList<Game> round = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] ids = line.trim().split(" ");
                int whiteId = Integer.parseInt(ids[0]);
                int blackId = Integer.parseInt(ids[1]);
                Player white = tournament.getPlayers().get(whiteId-1);
                Player black = blackId == 0 ? tournament.getPlayers().getBye() : tournament.getPlayers().get(blackId - 1);
                Game game = new Game(white, black);
                round.add(game);
                white.addRound(game);
                if(blackId != 0){
                    black.addRound(game);
                }
            }
            tournament.getRoundsObs().add(round);
            return round;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing) {
        return false;
    }

    public Tournament generateRandomTournament() {
        return null;
    }
}