package com.example.lolmanager.model;

import java.io.Serializable;
import java.util.UUID;

public class Game implements Serializable {
    transient private Player white;
    transient private Player black;
    private UUID whiteUUDI;
    private UUID blackUUID;
    private String whiteName;
    private String blackName;
    private Result whiteResult;
    private Result blackResult;
    private Boolean forfeit;

    public Game(Player white, Player black) {
        this(white, black, null, null, true);
    }

    Game(Player white, Player black, Result whiteResult, Result blackResult, Boolean forfeit) {
        setWhite(white);
        setBlack(black);
        setWhiteUUDI(white.getPlayerid());
        setBlackUUID(black.getPlayerid());
        setWhiteName(white.getName());
        setBlackName(black.getName());
        setForfeit(forfeit);
        setWhiteResult(whiteResult);
        setBlackResult(blackResult);
    }

    public Float getPointsForWhite() {
        switch (blackName) {
            case "bye" -> {
                return Player.getPointsForBye();
            }
            case "halfbye" -> {
                return Player.getPointsForHalfBye();
            }
            case "unpaired" -> {
                return 0F;
            }
        }

        if (getWhiteResult() == null) {
            return Float.NaN;
        }
        switch (getWhiteResult()) {
            case WIN -> {
                return Player.getPointsForWin();
            }
            case DRAW -> {
                return Player.getPointsForDraw();
            }
            case LOSE -> {
                return Player.getPointsForLose();
            }
            default -> {
                return Float.NaN;
            }
        }
    }

    public Float getPointsForBlack() {
        if (getBlackResult() == null) {
            return Float.NaN;
        }
        switch (getBlackResult()) {
            case WIN -> {
                return Player.getPointsForWin();
            }
            case DRAW -> {
                return Player.getPointsForDraw();
            }
            case LOSE -> {
                return Player.getPointsForLose();
            }
            default -> {
                return Float.NaN;
            }
        }
    }

    public Boolean isForfeit() {
        return forfeit;
    }

    public void swapPlayers() {
        Player white = getWhite();
        Player black = getBlack();
        Result whiteResult = getWhiteResult();
        Result blackResult = getBlackResult();
        setWhite(black);
        setBlack(white);
        setWhiteUUDI(getWhite().getPlayerid());
        setBlackUUID(getBlack().getPlayerid());
        setWhiteName(getWhite().getName());
        setBlackName(getBlack().getName());
        setWhiteResult(blackResult);
        setBlackResult(whiteResult);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Game other = (Game) obj;
        return this.getWhiteUUDI() == other.getWhiteUUDI() && this.blackUUID == other.getBlackUUID();
    }

    @Override
    public String toString() {
        return getWhiteName() +
                " " + getPointsForWhite() +
                " - " + getPointsForBlack() +
                " " + getBlackName();
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public UUID getWhiteUUDI() {
        return whiteUUDI;
    }

    public void setWhiteUUDI(UUID whiteUUDI) {
        this.whiteUUDI = whiteUUDI;
    }

    public UUID getBlackUUID() {
        return blackUUID;
    }

    public void setBlackUUID(UUID blackUUID) {
        this.blackUUID = blackUUID;
    }

    public Boolean getForfeit() {
        return forfeit;
    }

    public void setForfeit(Boolean forfeit) {
        this.forfeit = forfeit;
    }

    public Result getWhiteResult() {
        return whiteResult;
    }

    public void setWhiteResult(Result whiteResult) {
        this.whiteResult = whiteResult;
    }

    public Result getBlackResult() {
        return blackResult;
    }

    public void setBlackResult(Result blackResult) {
        this.blackResult = blackResult;
    }

    public Player getWhite() {
        return white;
    }

    public void setWhite(Player white) {
        this.white = white;
    }

    public Player getBlack() {
        return black;
    }

    public void setBlack(Player black) {
        this.black = black;
    }

}
