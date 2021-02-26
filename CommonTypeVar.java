package com.a2.crazyEightGame.constant;

public enum  CommonTypeVar {

    diamonds("diamonds"),
    clubs("clubs"),
    spade("spade"),
    red("red"),
    ;
    private String type;
    CommonTypeVar(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
