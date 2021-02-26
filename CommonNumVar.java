package com.a2.crazyEightGame.constant;

public enum CommonNumVar {
    A("A",1),
    J("J",10),
    Q("Q",10),
    K("K",10),
    N2("2",2),
    N3("3",3),
    N4("4",4),
    N5("5",5),
    N6("6",6),
    N7("7",7),
    N8("8",50),
    N9("9",9),
    N10("10",10),
    ;

    CommonNumVar(String number,int score){
        this.number=number;
        this.score=score;
    }
    private String number;
    private int score;

    public String getNumber() {
        return number;
    }

    public int getScore() {
        return score;
    }

}
