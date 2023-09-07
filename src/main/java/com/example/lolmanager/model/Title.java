package com.example.lolmanager.model;

import java.io.Serializable;

public enum Title implements Serializable {
    GM,
    IM,
    WGM,
    FM,
    WIM,
    CM,
    WFM,
    WCM,
    M,
    K_PLUS_PLUS,
    K_PLUS,
    K,
    I_PLUS_PLUS,
    I_PLUS,
    I,
    II_PLUS,
    II,
    III,
    IV,
    V,
    bk;

    public static Title getTitle(String title) {
        if (title == null) {
            return bk;
        }
        return switch (title.toUpperCase()) {
            case "GM" -> GM;
            case "IM" -> IM;
            case "WGM" -> WGM;
            case "FM" -> FM;
            case "WIM" -> WIM;
            case "CM" -> CM;
            case "WFM" -> WFM;
            case "WCM" -> WCM;
            case "M" -> M;
            case "K++" -> K_PLUS_PLUS;
            case "K+" -> K_PLUS;
            case "K" -> K;
            case "I++" -> I_PLUS_PLUS;
            case "I+" -> I_PLUS;
            case "I" -> I;
            case "II+" -> II_PLUS;
            case "II" -> II;
            case "III" -> III;
            case "IV" -> IV;
            case "V" -> V;
            default -> bk;
        };
    }

    @Override
    public String toString() {
        String value = super.toString();
        return value.replaceAll("_PLUS", "+");
    }
}
