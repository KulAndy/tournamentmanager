package com.example.tournamentmanager.model;

public enum ArbiterTitle {
    IA,
    FA,
    NA,
    PANSTWOWA,
    PIERWSZA,
    DRUGA,
    TRZECIA,
    MLODZIEZOWA;

    public static ArbiterTitle getAribterTitle(String symbol) {
        switch (symbol.toLowerCase()) {
            case "ia" -> {
                return IA;
            }
            case "fa" -> {
                return FA;
            }
            case "p", "państwowa" -> {
                return PANSTWOWA;
            }
            case "i", "pierwsza" -> {
                return PIERWSZA;
            }
            case "ii", "druga" -> {
                return DRUGA;
            }
            case "iii", "trzecia" -> {
                return TRZECIA;
            }
            case "m", "młodzieżowa" -> {
                return MLODZIEZOWA;
            }
            case "na" -> {
                return NA;
            }
            default -> {
                return null;
            }
        }
    }
}
