package com.a2.crazyEightGame.entity;

import java.io.Serializable;
import java.util.List;

public class PokerHand implements Serializable {

    private static final long serialVersionUID = 1488770273624015526L;
    private List<Poker> pokers;

    public PokerHand(List<Poker> lpokers) {
        this.pokers = lpokers;
    }

    public PokerHand(){}


    public void add(Poker poker){
        if(null!=poker)
            pokers.add(poker);
    }

    public Poker popPoker(int i) {
        if(i<pokers.size()){
            return pokers.remove(i);
        }
        return null;
    }

    public int getScores(){
       return pokers.stream().map(Poker::getScore).reduce(0, (a, b) -> a + b);
    }

    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }



    public List<Poker> getPokers() {
        return pokers;
    }
}
