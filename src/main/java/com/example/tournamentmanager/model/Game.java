package com.example.tournamentmanager.model;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class Game implements Serializable {
    transient private Player white;
    transient private Player black;
    private ObjectId whiteUUID;
    private ObjectId blackUUID;
    private String whiteName;
    private String blackName;
    private Result whiteResult;
    private Result blackResult;
    private Boolean forfeit;

    public Game(Player white, Player black) {
        this(white, black, null, null, true);
    }

    public Game(Player white, Player black, Result whiteResult, Result blackResult, Boolean forfeit) {
        setWhite(white);
        setBlack(black);
        setWhiteUUID(white.getPlayerid());
        setBlackUUID(black.getPlayerid());
        setWhiteName(white.getName());
        setBlackName(black.getName());
        setForfeit(forfeit);
        setWhiteResult(whiteResult);
        setBlackResult(blackResult);
    }

    public Player getLoser() {
        if (
                getWhiteResult() == Result.WIN && getBlackResult() == Result.LOSE
        ) {
            return getBlack();
        } else if (getWhiteResult() == Result.LOSE && getBlackResult() == Result.WIN) {
            return getWhite();
        } else {
            return null;
        }

    }

    public Player getWinner() {
        if (getWhiteName().equals("bye")) {
            return getBlack();
        } else if (getBlackName().equals("bye")) {
            return getWhite();
        } else if (
                getWhiteResult() == Result.WIN && getBlackResult() == Result.LOSE
        ) {
            return getWhite();
        } else if (getWhiteResult() == Result.LOSE && getBlackResult() == Result.WIN) {
            return getBlack();
        } else {
            return null;
        }
    }

    public Float getPointsForWhite() {
        switch (blackName) {
            case "bye" -> {
                return Player.getByePoints();
            }
            case "halfbye" -> {
                return Player.getHalfByePoints();
            }
            case "unpaired" -> {
                return 0F;
            }
        }

        if (getWhiteResult() == null) {
            return 0F;
        }
        switch (getWhiteResult()) {
            case WIN -> {
                return Player.getWinPoints();
            }
            case DRAW -> {
                return Player.getDrawPoints();
            }
            case LOSE -> {
                return Player.getLosePoints();
            }
            default -> {
                return Float.NaN;
            }
        }
    }

    public Float getPointsForBlack() {
        if (getBlackResult() == null) {
            return 0F;
        }
        switch (getBlackResult()) {
            case WIN -> {
                return Player.getWinPoints();
            }
            case DRAW -> {
                return Player.getDrawPoints();
            }
            case LOSE -> {
                return Player.getLosePoints();
            }
            default -> {
                return Float.NaN;
            }
        }
    }

    public void swapPlayers() {
        Player white = getWhite();
        Player black = getBlack();
        Result whiteResult = getWhiteResult();
        Result blackResult = getBlackResult();
        setWhite(black);
        setBlack(white);
        setWhiteUUID(getWhite().getPlayerid());
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
        return this.getWhiteUUID() == other.getWhiteUUID() && this.blackUUID == other.getBlackUUID();
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

    public ObjectId getWhiteUUID() {
        return whiteUUID;
    }

    public void setWhiteUUID(ObjectId whiteUUID) {
        this.whiteUUID = whiteUUID;
    }

    public ObjectId getBlackUUID() {
        return blackUUID;
    }

    public void setBlackUUID(ObjectId blackUUID) {
        this.blackUUID = blackUUID;
    }

    public Boolean isForfeit() {
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
        if (white != null) {
            setWhiteUUID(white.getPlayerid());
            setWhiteName(white.getName());
        }
    }

    public Player getBlack() {
        return black;
    }

    public void setBlack(Player black) {
        this.black = black;
        if (black != null) {
            setBlackUUID(black.getPlayerid());
            setBlackName(black.getName());
        }
    }

}
