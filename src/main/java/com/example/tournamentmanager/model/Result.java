package com.example.tournamentmanager.model;

import java.io.Serializable;

public enum Result implements Serializable {
    WIN,
    DRAW,
    LOSE;

    public static String getResultString(Result result, boolean isForfeit) {
        if (result == null) {
            return "";
        }
        if (isForfeit) {
            switch (result) {
                case WIN -> {
                    return "+";
                }
                case LOSE -> {
                    return "-";
                }
            }
        }
        switch (result) {
            case WIN -> {
                return "1";
            }
            case DRAW -> {
                return "0.5";
            }
            case LOSE -> {
                return "0";
            }
            default -> {
                return "";
            }
        }
    }

    public static Object[] getResultFromPoints(String result) {
        return switch (result.trim()) {
            case "1" -> new Object[]{WIN, false};
            case "1/2", "0.5", "0,5" -> new Object[]{DRAW, false};
            case "+" -> new Object[]{WIN, true};
            case "-" -> new Object[]{LOSE, true};
            case "" -> new Object[]{null, true};
            default -> new Object[]{LOSE, false};
        };
    }

    public static Object[] getResultsFromPoints(String result) {
        String[] results = result.split("-");
        return new Object[]{getResultFromPoints(results[0])[0], getResultFromPoints(results[1])[0], (boolean) getResultFromPoints(results[0])[1] || (boolean) getResultFromPoints(results[1])[1]};
    }

    public static String[] getPossibleResults() {
        return new String[]{
                "",
                "1-0",
                "0.5-0.5",
                "0-1",
                "+--",
                "--+",
                "0-0",
                "---",
                "0.5-0",
                "0-0.5",
        };
    }
}
