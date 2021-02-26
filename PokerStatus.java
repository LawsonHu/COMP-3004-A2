package com.a2.crazyEightGame.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PokerStatus implements Serializable {
    private static final long serialVersionUID = -529180601014008738L;
    private Poker pokerTop;
    private PokerHand pokerHand;
    private PokerDui pokerDui;
    private List<Poker> oldTopPoker=new ArrayList<>();

    public PokerStatus(){}
    public PokerStatus(Poker pokerTop, PokerHand pokerHand, PokerDui pokerDui) {
        this.pokerTop = pokerTop;
        this.pokerHand = pokerHand;
        this.pokerDui = pokerDui;
    }

    public Poker getPokerTop() {
        return pokerTop;
    }

    public boolean gameover(){
        if(pokerHand.getPokers().size()==0){
            return true;
        }
        if(pokerDui.getPokers().get(0).getNumber().equals("JOKER")){
            return true;
        }
        return false;
    }

    public void setPokerTop(Poker pokerTop) {
        this.pokerTop = pokerTop;
    }

    public PokerHand getPokerHand() {
        return pokerHand;
    }

    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    public PokerDui getPokerDui() {
        return pokerDui;
    }

    public void setPokerDui(PokerDui pokerDui) {
        this.pokerDui = pokerDui;
    }

    public List<Poker> getOldTopPoker() {
        return oldTopPoker;
    }

    public void setOldTopPoker(List<Poker> oldTopPoker) {
        this.oldTopPoker = oldTopPoker;
    }
    public void addOldTopPoker(Poker poker) {
        this.oldTopPoker.add(poker);
    }
}
