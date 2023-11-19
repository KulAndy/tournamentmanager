package com.example.andchess.model;

public enum ArbiterTitle {
    IA,
    FA,
    NA,
    PAŃSTWOWA,
    PIERWSZA,
    DRUGA,
    TRZECIA,
    MŁODZIEŻOWA;

    public static ArbiterTitle getAribterTitle(String symbol) {
        switch (symbol.toLowerCase()) {
            case "ia" -> {
                return IA;
            }
            case "fa" -> {
                return FA;
            }
            case "p", "państwowa" -> {
                return PAŃSTWOWA;
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
                return MŁODZIEŻOWA;
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
