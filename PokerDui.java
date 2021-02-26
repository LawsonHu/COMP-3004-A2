package com.a2.crazyEightGame.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokerDui implements Serializable {

    private static final long serialVersionUID = 4804121802749312847L;
    private List<Poker> pokers=new ArrayList<>();

    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }

    public List<Poker> getPokers() {
        return pokers;
    }

    public void add(Poker poker){
        pokers.add(poker);
    }


    public Poker pop() {
        if(pokers.size()>0){
            return pokers.remove(0);
        }
        return null;
    }

    public PokerHand pop(int count) {
        if(pokers.size()>count){
            List<Poker> lpokers=new ArrayList<>();
            for (int i=0;i<count;i++){
                lpokers.add(pokers.remove(i));
            }
            PokerHand pokerHand=new PokerHand(lpokers);
            return pokerHand;
        }
        return null;
    }

    public Poker popPoker(int i) {
        if(i<pokers.size()){
            return pokers.remove(i);
        }
        return null;
    }

    public Poker popTop() {
        if(pokers.size()>0){
            // if top is 8
            while("8".equals(pokers.get(0).getNumber())){
                Random r=new Random();
                int k=r.nextInt(pokers.size());
                Poker poker=pokers.get(0);
                pokers.set(0,pokers.get(k));
                pokers.set(k,poker);
            }
            return pokers.remove(0);
        }
        return null;
    }
}
