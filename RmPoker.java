package com.a2.crazyEightGame.alg;

import com.a2.crazyEightGame.entity.Poker;

import java.io.Serializable;
import java.util.Random;

public class RmPoker implements Serializable {


    private static final long serialVersionUID = -7745765724482261762L;

    public static void swap(int[] s, int a, int b) {
        s[a] = s[a] ^ s[b];
        s[b] = s[a] ^ s[b];
        s[a] = s[a] ^ s[b];
    }


    public static int[] shuffle2(int[] s) {
        Random random = new Random();
        for (int i = 0; i < s.length; i++) {
            int num = random.nextInt(s.length);
            if (num != i) {
                swap(s, num, i);
            }
        }
        return s;
    }

    public static boolean canPlayOut(Poker p, Poker p1) {

        if (null == p1) {
            return false;
        }
        if ("8".equals(p1.getNumber())) {
            p.setNumber("8");
            p.setType(p1.getType());
            return true;
        } else if (p1.getNumber().equals(p.getNumber())) {
            p.setType(p1.getType());
            return true;
        } else if (p1.getType().equals(p.getType())) {
            p.setNumber(p1.getNumber());
            return true;
        } else {
            return false;
        }
    }

    public static boolean canPlayOutOnlyJudge(Poker p, Poker p1) {

        if (null == p1) {
            return false;
        }
        if ("8".equals(p1.getNumber())) {
            return true;
        } else if (p1.getNumber().equals(p.getNumber())) {
            return true;
        } else if (p1.getType().equals(p.getType())) {
            return true;
        } else {
            return false;
        }
    }

    public static String setSimpleType(String type) {
        switch (type){
            case "H":
                return "red";
            case "D":
                return "diamonds";
            case "C":
                return "clubs";

            case "S":
                return "spade";
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static int getScore(String number) {
        switch (number){
            case "A":
                return 1;
            case "K":
                return 10;
            case "Q":
                return 10;
            case "J":
                return 10;
            case "10":
                return 10;
            case "9":
                return 9;
            case "8":
                return 8;
            case "7":
                return 7;
            case "6":
                return 6;
            case "5":
                return 5;
            case "4":
                return 4;
            case "3":
                return 3;
            case "2":
                return 2;
            default:
                return 0;
        }
    }

    public static boolean canPlayOutTwoOnlyJudge(Poker pokerTop, Poker poker) {
        if(pokerTop.getNumber().equals("2")&&poker.getNumber().equals("2")){
            return true;
        }
        return false;
    }
}
