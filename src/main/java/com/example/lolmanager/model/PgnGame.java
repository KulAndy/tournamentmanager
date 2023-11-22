package com.example.lolmanager.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PgnGame {
    private String white = "N, N";
    private String black = "N, N";
    private Short year = null;
    private Byte month = null;
    private Byte day = null;
    private String event = "?";
    private String site = "?";
    private Byte round = null;
    private String result = "*";
    private String moves = "";
    private short whiteElo = 0;
    private short blackElo = 0;

    public PgnGame(){
        setMoves("1. *");
    }

    public PgnGame(Player white, Player black, Date date, Tournament tournament){
        Game game = white.getRound(black);
        setWhite(white.getName());
        setWhiteElo(white.getFideRating().shortValue());
        setBlack(black.getName());
        setBlackElo(black.getFideRating().shortValue());
        setEvent(tournament.getName());
        setSite(tournament.getPlace());
        if (game != null){
            if (game.getWhiteResult() != null && game.getBlackResult() != null){
                StringBuilder result = getResultTag(game);
                setResult(result.toString());
            }
            int round = white.getRounds().indexOf(game);
            if (round >= 0){
                setRound((byte) (round + 1));
            }else {
                setRound((byte) tournament.getRounds().size());
            }
        }else {
            setRound((byte) tournament.getRounds().size());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        setDate(dateFormat.format(date));
        setMoves("1. *");
    }

    private static StringBuilder getResultTag(Game game) {
        StringBuilder result = new StringBuilder();
        switch (game.getWhiteResult()){
            case WIN -> result.append("1");
            case DRAW -> result.append("1/2");
            case LOSE -> result.append("0");
        }
        result.append("-");
        switch (game.getBlackResult()){
            case WIN -> result.append("1");
            case DRAW -> result.append("1/2");
            case LOSE -> result.append("0");
        }
        return result;
    }

    public PgnGame(File pgn){
        try (BufferedReader reader = new BufferedReader(new FileReader(pgn))) {
            String line;
            boolean startReading = false;
            byte counter = 0;
            while ((line = reader.readLine()) != null && counter < 2) {
                if (!startReading && line.trim().isEmpty()) {
                    continue;
                } else {
                    startReading = true;
                    if (line.isEmpty()){
                        counter++;
                    }else {
                        if (line.startsWith("[")){
                            int keyStartIndex = line.indexOf('[');
                            int keyEndIndex = line.indexOf(' ');
                            int valueStartIndex = line.indexOf('"');
                            int valueEndIndex = line.indexOf('"', valueStartIndex);

                            String key = line.substring(keyStartIndex + 1, keyEndIndex).trim();
                            String value = line.substring(valueStartIndex + 1, valueEndIndex).trim();

                            switch (key.toLowerCase()){
                                case "event" -> setEvent(value);
                                case "site" -> setSite(value);
                                case "date" -> setDate(value);
                                case "round" -> setRound(value);
                                case "white" -> setWhite(value);
                                case "black" -> setBlack(value);
                                case "result" -> setResult(value);
                                case "whiteelo" -> setWhiteElo(value);
                                case "blackelo" -> setBlackElo(value);
                            }
                        }else {
                            setMoves(getMoves() + line);
                        }
                    }
                }
            }
        } catch (IOException ignored) {}

        if (moves.length() == 0){
            setMoves("1. *");
        }

    }

    public String getPgn(){
        StringBuilder builder = new StringBuilder();
        builder.append("[Event \"").append(getEvent()).append("\"]").append("\n");
        builder.append("[Site \"").append(getSite()).append("\"]").append("\n");
        builder.append("[Date \"").append(getDate()).append("\"]").append("\n");
        builder.append("[Round \"");
        if (getRound() == null){
            builder.append("?").append("\"]").append("\n");
        }else{
            builder.append(getRound()).append("\"]").append("\n");
        }
        builder.append("[White \"").append(getWhite()).append("\"]").append("\n");
        builder.append("[Black \"").append(getBlack()).append("\"]").append("\n");
        builder.append("[Result \"").append(getResultTag()).append("\"]").append("\n");
        if (getWhiteElo() > 0){
            builder.append("[WhiteElo \"").append(getWhiteElo()).append("\"]").append("\n");
        }
        if (getBlackElo() > 0){
            builder.append("[BlackElo \"").append(getBlackElo()).append("\"]").append("\n");
        }

        builder.append("\n");
        builder.append(getMoves()).append("\n");

        return builder.toString();
    }

    public PgnGame(String pgn){
        try (BufferedReader reader = new BufferedReader(new StringReader(pgn))) {
            String line;
            boolean startReading = false;
            byte counter = 0;
            while ((line = reader.readLine()) != null && counter < 2) {
                if (!startReading && line.trim().isEmpty()) {
                    continue;
                }
                startReading = true;
                if (line.isEmpty()){
                    counter++;
                }else {
                    if (line.startsWith("[")){
                        int keyStartIndex = line.indexOf('[');
                        int keyEndIndex = line.indexOf(' ');
                        int valueStartIndex = line.indexOf('"');
                        int valueEndIndex = line.indexOf('"', valueStartIndex + 1);

                        String key = line.substring(keyStartIndex + 1, keyEndIndex).trim();
                        String value = line.substring(valueStartIndex + 1, valueEndIndex).trim();

                        switch (key.toLowerCase()){
                            case "event" -> setEvent(value);
                            case "site" -> setSite(value);
                            case "date" -> setDate(value);
                            case "round" -> setRound(value);
                            case "white" -> setWhite(value);
                            case "black" -> setBlack(value);
                            case "result" -> setResult(value);
                            case "whiteelo" -> setWhiteElo(value);
                            case "blackelo" -> setBlackElo(value);
                        }
                    }else {
                        setMoves(getMoves() + line);
                    }
                }
            }
        } catch (IOException ignored) {}

        if (moves.length() == 0){
            setMoves("1. *");
        }
    }

    public void setDate(String date){
        try{
            String[] dateArray = date.split("\\.");
            try{
                setYear(Short.parseShort(dateArray[0]));
            } catch (NumberFormatException e) {
                setYear(null);
            }
            try{
                setMonth(Byte.parseByte(dateArray[1]));
            } catch (NumberFormatException e) {
                setMonth(null);
            }
            try{
                setDay(Byte.parseByte(dateArray[2]));
            } catch (NumberFormatException e) {
                setDay(null);
            }
        } catch (Exception e) {
            setYear(null);
        }
    }

    public String getDate(){
        StringBuilder builder = new StringBuilder();
        if (getYear() == null || getYear() <= 0){
            builder.append("????");
        }else {
            builder.append(getYear());
        }
        builder.append(".");
        if (getMonth() == null || getMonth() <= 0){
            builder.append("??");
        }else {
            builder.append(getMonth());
        }
        builder.append(".");
        if (getDay() == null || getDay() <= 0){
            builder.append("??");
        }else {
            builder.append(getDay());
        }

        return builder.toString();
    }
    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Byte getMonth() {
        return month;
    }

    public void setMonth(Byte month) {
        this.month = month;
    }

    public Byte getDay() {
        return day;
    }

    public void setDay(Byte day) {
        this.day = day;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Byte getRound() {
        return round;
    }

    public void setRound(Byte round) {
        this.round = round;
    }

    public void setRound(String round) {
        try{
            int pointIndex = round.indexOf('.');
            if (pointIndex < 0){
                setRound(Byte.parseByte(round));
            }else {
                setRound(Byte.parseByte(round.substring(0, pointIndex)));
            }
        } catch (NumberFormatException e) {
            this.round = null;
        }
    }

    public String getResultTag() {
        return result;
    }

    public void setResult(String result) {
        result = result.trim().replaceAll("\\s+", "");
        switch (result){
            case "1-0", "0-1", "1/2-1/2", "0-0", "+--", "--+" -> this.result = result;
            case "0.5-0.5", "0,5-0,5" -> this.result = "1/2-1/2";
            default -> this.result = "*";
        }
    }

    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public short getWhiteElo() {
        return whiteElo;
    }

    public void setWhiteElo(short whiteElo) {
        this.whiteElo = whiteElo;
    }

    public void setWhiteElo(String whiteElo) {
        try{
            setWhiteElo(Short.parseShort(whiteElo));
        } catch (NumberFormatException e) {
            setWhiteElo((short) 0);
        }
    }
    public short getBlackElo() {
        return blackElo;
    }

    public void setBlackElo(short blackElo) {
        this.blackElo = blackElo;
    }
    public void setBlackElo(String blackElo) {
        try{
            setBlackElo(Short.parseShort(blackElo));
        } catch (NumberFormatException e) {
            setBlackElo((short) 0);
        }
    }
}
