package com.example.lolmanager.model;

public class Withdraw {
    private Player player;
    private WithdrawType type;

    private Byte roundNo;

    public Withdraw(Player player, WithdrawType type) {
        this(player, type, (byte) 0);
    }

    public Withdraw(Player player, WithdrawType type, Byte roundNo) {
        setPlayer(player);
        setType(type);
        setRoundNo(roundNo);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public WithdrawType getType() {
        return type;
    }

    public void setType(WithdrawType type) {
        this.type = type;
    }

    public Byte getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(Byte roundNo) {
        this.roundNo = roundNo;
    }

    public enum WithdrawType {
        TOURNAMENT,
        ROUND,
        HALFBYE
    }
}
