package com.a2.crazyEightGame.entity;

public class CurrentPoker {
    private String msg;
    private Poker poker;

    public CurrentPoker(){}

    public CurrentPoker(String msg, Poker poker) {
        this.msg = msg;
        this.poker = poker;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Poker getPoker() {
        return poker;
    }

    public void setPoker(Poker poker) {
        this.poker = poker;
    }
}
