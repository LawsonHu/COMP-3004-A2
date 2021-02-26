package com.a2.crazyEightGame.entity;


import cucumber.api.java.eo.Se;

import java.io.Serializable;

public class Poker implements Serializable {

    private static final long serialVersionUID = -5025740729127426677L;
    private String type;
    private String number;

    private int score;

    public Poker(){}

    public Poker(String type,String number,int score) {
        this.number = number;
        this.type = type;
        this.score = score;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        String tp=null;
        /*
        switch (type){
            case "red":
                tp="♥️";
                break;
            case "diamonds":
                tp="♦️️";
                break;
            case "clubs":
                tp="♣️️️";
                break;
            case "spade":
                tp="♠️";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        */
        switch (type){
            case "red":
                tp="H";
                break;
            case "diamonds":
                tp="D";
                break;
            case "clubs":
                tp="C";
                break;
            case "spade":
                tp="S";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return "{" +tp+ number +"}";
    }

    public void setSimpleType(String type) {
        switch (type){
            case "H":
                this.type="red";
                break;
            case "D":
                this.type="diamonds";
                break;
            case "C":
                this.type="clubs";
                break;
            case "S":
                this.type="spade";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}

