package com.example.tournamentmanager.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.tournamentmanager.operation.FIDEOperation.saveTrfReport;
import static com.example.tournamentmanager.operation.FIDEOperation.trfReport;

public class TieBreakServerWrapper {
    private static final String pythonPath = "python3";
    private static final String tiebreakChecker = "tiebreak_server/tiebreakchecker.py";
    private static final String outputFilePath = "tiebreak.json";
    private static final String reportFilePath = "report.txt";
    private static String tieBreakJSON = "";
    private static int currentRound = 0;

    public static boolean generateTiebreak(Tournament tournament, int round) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Gson gson = new Gson();
        String json = gson.toJson(tournament.getTiebreak());
        if (
                currentRound == round &&
                        Objects.equals(json, tieBreakJSON)
        ) {
            return false;
        }
        File outputFile = new File(outputFilePath);
        File reportFile = new File(reportFilePath);

        if (outputFile.exists()) {
            outputFile.delete();
        }
        if (reportFile.exists()) {
            reportFile.delete();
        }

        saveTrfReport(trfReport(tournament), reportFile);
        List<String> command = buildCommand(tournament, round, reportFile, outputFile);

        if (executeProcess(command) != 0){
            return false;
        }
        processCompetitors(outputFile, tournament);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        if (reportFile.exists()) {
            reportFile.delete();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time in milliseconds: " + elapsedTime);
        currentRound = round;
        tieBreakJSON = json;
        return true;
    }

    private static List<String> buildCommand(Tournament tournament, int round, File reportFile, File outputFile) {
        List<String> command = new ArrayList<>();
        command.add(pythonPath);
        command.add(tiebreakChecker);
        command.add("-i");
        command.add(reportFile.getAbsolutePath());
        command.add("-o");
        command.add(outputFile.getAbsolutePath());
        if (round != 0) {
            command.add("-n");
            command.add(String.valueOf(round));
        }
        command.add("-t");
        command.addAll(getTiebreakMethods(tournament));
        return command;
    }

    private static List<String> getTiebreakMethods(Tournament tournament) {
        List<String> methods = new ArrayList<>();
        methods.add(methodShort(tournament.getTiebreak().getTiebreak1()));
        methods.add(methodShort(tournament.getTiebreak().getTiebreak2()));
        methods.add(methodShort(tournament.getTiebreak().getTiebreak3()));
        methods.add(methodShort(tournament.getTiebreak().getTiebreak4()));
        methods.add(methodShort(tournament.getTiebreak().getTiebreak5()));
        return methods;
    }

    private static int executeProcess(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        return process.waitFor();
    }

    private static void processCompetitors(File outputFile, Tournament tournament) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            parseAndProcessCompetitors(jsonStringBuilder.toString(), tournament);
        }
    }

    private static void parseAndProcessCompetitors(String jsonString, Tournament tournament) {
        JsonObject rootObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject statusObject = rootObject.getAsJsonObject("status");
        JsonArray competitorsArray = statusObject.getAsJsonArray("competitors");

        PlayerList players = tournament.getPlayers();
        players.sort(players.getComparator());
        for (int i = 0; i < competitorsArray.size(); i++) {
            JsonObject competitor = competitorsArray.get(i).getAsJsonObject();
            JsonArray tiebreaks = competitor.getAsJsonArray("tieBreak");
            int startno = competitor.get("startno").getAsInt();
            Player player = players.get(startno - 1);

            player.setTb1(tiebreaks.get(0).getAsFloat());
            player.setTb2(tiebreaks.get(1).getAsFloat());
            player.setTb3(tiebreaks.get(2).getAsFloat());
            player.setTb4(tiebreaks.get(3).getAsFloat());
            player.setTb5(tiebreaks.get(4).getAsFloat());
        }
    }

    private static String methodShort(Tournament.Tiebreak.TbMethod method) {
        if (method == null) {
            return "";
        }
        return switch (method) {
            case POINTS -> "PTS";
            case DUEL -> "DE";
            case WINS -> "WON";
            case GAMES_WITH_BLACK -> "BPG";
            case WINS_WITH_BLACK -> "BWG";
            case PROGRESS -> "PS";
            case BUCHOLZ -> "BH";
            case BUCHOLZ_CUT1 -> "BH/C1";
            case MEDIA_BUCHOLZ -> "BH/M1";
            case FORE_BUCHOLZ -> "FB";
            case SONNEN_BERGER -> "SB";
            case KOYA -> "KS";
            case START_NUMBER -> "SNO";
            case RANDOM -> "RND";
            case AVERAGE_OPPONENTS_BUCHOLZ -> "AOB";
            case RATING_PERFORMANCE -> "TPR";
            case PERFECT_PERFORMANCE -> "PTP";
            case AVERAGE_OPPONENTS_RATING_PERFORMANCE -> "APRO";
            case AVERAGE_OPPONENTS_PERFECT_PERFORMANCE -> "APPO";
        };
    }
}
