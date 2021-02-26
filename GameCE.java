package com.a2.crazyEightGame.entity;


import com.a2.crazyEightGame.PlayerCE;
import com.a2.crazyEightGame.constant.CommonNumVar;
import com.a2.crazyEightGame.constant.CommonTypeVar;
import com.a2.crazyEightGame.entity.Poker;
import com.a2.crazyEightGame.entity.PokerDui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.a2.crazyEightGame.alg.RmPoker.shuffle2;


public class GameCE implements Serializable {

    private static final long serialVersionUID = 2278256458056486631L;


    public PokerDui preGame() {

        List<CommonNumVar> numlist = Arrays.asList(CommonNumVar.A,
                CommonNumVar.J,
                CommonNumVar.Q,
                CommonNumVar.K,
                CommonNumVar.N2,
                CommonNumVar.N3,
                CommonNumVar.N4,
                CommonNumVar.N5,
                CommonNumVar.N6,
                CommonNumVar.N7,
                CommonNumVar.N8,
                CommonNumVar.N9,
                CommonNumVar.N10
        );

        List<String> typelist = Arrays.asList(
                CommonTypeVar.clubs.getType(),
                CommonTypeVar.spade.getType(),
                CommonTypeVar.diamonds.getType(),
                CommonTypeVar.red.getType()
        );
        PokerDui pokerDui = new PokerDui();

        for (int i = 0; i < typelist.size(); i++) {
            for (int j = 0; j < numlist.size(); j++) {
                Poker poker = new Poker(typelist.get(i), numlist.get(j).getNumber(), numlist.get(j).getScore());
                pokerDui.add(poker);
            }
        }


        int[] sin = IntStream.rangeClosed(0, 51).toArray();
        sin = shuffle2(sin);

        PokerDui pokerDuiRm = new PokerDui();
        for (int i = 0; i < pokerDui.getPokers().size(); i++) {
            pokerDuiRm.add(pokerDui.getPokers().get(sin[i]));
        }
//        pokerDuiRm.getPokers().stream().forEach(System.out::println);
        return pokerDuiRm;
    }

//    public static void main(String[] args) {
//        GameCE gameCE=new GameCE();
//
//        PokerDui prePoker = gameCE.preGame();
//
//        PlayerCE player1=new PlayerCE();
//        PokerHand pokerHand1=prePoker.pop(5);
//        player1.setPokerHand(pokerHand1);
//
//        System.out.println(prePoker.pop());
//
//        System.out.println("player1:");
//        System.out.println(pokerHand1.getPokers());
//
//
//    }


    public PlayerCE getWinner(PlayerCE[] players) {
        int[] arr = new int[players.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = players[i].getScore();
        }
        int min = arr[0];
        int index=0;
        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
                index=i;
            }
        }
        return players[index];
    }
    
}
