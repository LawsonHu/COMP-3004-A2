package com.a2.crazyEightGame;

import com.a2.crazyEightGame.alg.RmPoker;
import com.a2.crazyEightGame.entity.CurrentPoker;
import com.a2.crazyEightGame.entity.Poker;
import com.a2.crazyEightGame.entity.PokerHand;
import com.a2.crazyEightGame.entity.PokerStatus;
import gherkin.deps.com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class PlayerCE implements Serializable {

    private static final long serialVersionUID = 963959669330472632L;
    private String name;
    private PokerHand pokerHand;
    private int score;
    private int turnNum=0;
    int willJoinPlayers;

    int playerId = 0;
    public PlayerCE(){}
    public PlayerCE(String name){
        this.name=name;

    }

    public void setTurnNum(int turnNum) {
        this.turnNum = turnNum;
    }

    public int getTurnNum() {
        return turnNum;
    }

    private int[] scoreSheet = new int[15];

    static Client clientConnection;

    PlayerCE[] players;


    public void setName(String name) {
        this.name = name;
    }

    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    public int getScore() {
        return Arrays.stream(scoreSheet).sum();
    }

    public PokerHand getPokerHand() {
        return pokerHand;
    }

    public String getName() {
        return name;
    }

    public int[] getScoreSheet() {
        return scoreSheet;
    }

    public void setScoreSheet(int[] scoreSheet) {
        this.scoreSheet = scoreSheet;
    }

    public PlayerCE getPlayer() {
        return this;
    }

    /*
     * ----------Network Stuff------------
     */

    /*
     * send the to do to test server
     */
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void connectToClient(String host) {
        clientConnection = new Client(host);
    }

    public void connectToClient(int port) {
        clientConnection = new Client(port);
    }

    public void initializePlayers() {
        for (int i = 0; i < willJoinPlayers; i++) {
            players[i] = new PlayerCE(" ");
        }
    }
    /*
     * update turns
     */
    public void printPlayerScores(PlayerCE[] pl) {
        for(int i=0;i<pl.length;i++){
            printScoreSheet(pl[i]);
        }

    }

    /*
     * prints the score sheet
     */
    public void printScoreSheet(PlayerCE p) {
        System.out.println("Turn "+p.getTurnNum()+" Player " + p.getName() + "'s score is");
        for (int i = 0; i < this.getTurnNum(); i++) {
            System.out.print(p.getScoreSheet()[i] + ",");
        }
        System.out.println("  sum="+p.getScore());
    }


    public void startGame() {
        // receive players once for names
        players = clientConnection.receivePlayer();
        Scanner sc=new Scanner(System.in);

        while (true) {
            int round = clientConnection.receiveRoundNo();
            if (round == -1)
                break;
            if (round == -4){
                Poker poker=clientConnection.receivePorkTop();
                System.out.println("Current poker is "+poker);
                continue;
            }
            if (round == -3){
                CurrentPoker currentPoker= (CurrentPoker) clientConnection.receiveObject(CurrentPoker.class);
                System.out.println(currentPoker.getMsg()+currentPoker.getPoker());
                continue;
            }
            if (round == -2){
                String currentmsg= (String) clientConnection.receiveObject(String.class);
                System.out.println(currentmsg);
                continue;
            }

            System.out.println("\n \n \n ********Round Number " + round + "********");
            System.out.println("This is your round!");
            int[][] pl = clientConnection.receiveScores();
            for (int i = 0; i < willJoinPlayers; i++) {
                players[i].setScoreSheet(pl[i]);
            }
            printPlayerScores(players);

            PokerStatus pokerStatus = clientConnection.receivePokerStatus();
            System.out.println("The discard pile top poker is "+pokerStatus.getPokerTop());

            boolean canPlay=false;
            int canPlaynum=0;
            tag1:
            for(int i=0;i<pokerStatus.getPokerHand().getPokers().size();i++){
                if(RmPoker.canPlayOutOnlyJudge(pokerStatus.getPokerTop(),pokerStatus.getPokerHand().getPokers().get(i))){
                    canPlay=true;
                    break tag1;
                }
            }
            if(!canPlay){
                System.out.println("Can't play...");
                System.out.println("Your hand poker is "+pokerStatus.getPokerHand().getPokers());
                tag2:
                for(int i=0;i<3;i++) {
                    Poker po = pokerStatus.getPokerDui().pop();
                    pokerStatus.getPokerHand().add(po);
                    canPlay=RmPoker.canPlayOutOnlyJudge(pokerStatus.getPokerTop(),po);
                    if(canPlay) {
                        System.out.println("Can play!");
                        canPlaynum=pokerStatus.getPokerHand().getPokers().size();
                        break tag2;
                    }
                    System.out.println("Can't play...");
                    System.out.println("Your hand poker is "+pokerStatus.getPokerHand().getPokers());
                }

                if(!canPlay) {
                    System.out.println("Can't play any poker!");
                    Poker pokerTop = pokerStatus.getPokerTop();
                    pokerStatus.setPokerTop(pokerTop);
                    System.out.println("You must immediately your turn. Now,your handpoker is:");
                    System.out.println(pokerStatus.getPokerHand().getPokers());
                    pokerStatus.getPokerHand().setPokers(pokerStatus.getPokerHand().getPokers());
                    pokerStatus.setPokerHand(pokerStatus.getPokerHand());
                    pokerStatus.setPokerDui(pokerStatus.getPokerDui());
                    clientConnection.sendPokerStatus(pokerStatus);
                    continue;
                }
            }
            System.out.println("Your hand poker is "+pokerStatus.getPokerHand().getPokers());


            int choose=0;
            while(choose==0 || choose>pokerStatus.getPokerHand().getPokers().size() || !RmPoker.canPlayOut(pokerStatus.getPokerTop(),pokerStatus.getPokerHand().getPokers().get(choose-1))){
                System.out.println(choose==0?"Choose a card to play: (1 or 2...) ":"Can't play,Please choose a real card to play: (1 or 2...) ");
                boolean flag=true;
                while (flag){
                    try{
                        choose=sc.nextInt();
                        flag=false;
                    }catch (Exception e){
                    }
                }
                if(choose==0||choose>pokerStatus.getPokerHand().getPokers().size()){
                    if(pokerStatus.getPokerDui().getPokers().size()==0){
                        choose=-1;
                        pokerStatus.getPokerDui().getPokers().add(new Poker("NULL","JOKER",0));
                        System.out.println("not any more card! ");
                        pokerStatus.setPokerDui(pokerStatus.getPokerDui());
                        pokerStatus.setPokerHand(pokerStatus.getPokerHand());
                        clientConnection.sendPokerStatus(pokerStatus);
                        break;
                    }
                    choose=canPlaynum;
                }
            }
            if(choose!=-1){
                if(pokerStatus.getPokerHand().getPokers().get(choose-1).getNumber().equals("8")) {

                    System.out.println("Please declares next suit,H or D or C or S");
                    boolean f=true;
                    while (f) {
                        try {
                            String se = sc.next();
                            pokerStatus.getPokerHand().getPokers().get(choose - 1).setType(RmPoker.setSimpleType(se));
                            f=false;
                        }catch (Exception e){
                            System.out.println("Please declares next suit,H or D or C or S");
                        }
                    }
                }
                System.out.println("play "+pokerStatus.getPokerHand().getPokers().get(choose-1));
                Poker pokerTop = pokerStatus.getPokerHand().getPokers().remove(choose - 1);
                pokerStatus.setPokerTop(pokerTop);
                pokerStatus.getPokerHand().setPokers(pokerStatus.getPokerHand().getPokers());
                pokerStatus.setPokerHand(pokerStatus.getPokerHand());
                pokerStatus.setPokerDui(pokerStatus.getPokerDui());
                clientConnection.sendPokerStatus(pokerStatus);
            }


        }

    }

    public PlayerCE returnWinner() {
        try {
            int[][] pl = clientConnection.receiveScores();
            for (int i = 0; i < willJoinPlayers; i++) {
                players[i].setScoreSheet(pl[i]);
            }
            printPlayerScores(players);
            PlayerCE win = (PlayerCE) clientConnection.dIn.readObject();
            if (playerId == win.playerId) {
                System.out.println("This turn You win!");
            } else {
                System.out.println("This turn the winner is " + win.name);
            }
            return win;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        private Gson gson = new Gson();


        public Client(String host) {
            try {
                socket = new Socket(host, 3333);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public Client(int portId) {
            try {
                socket = new Socket("localhost", portId);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        /*
         * function to send the score sheet to the server
         */
        public void sendPlayer() {
            try {
                dOut.writeObject(getPlayer());
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }

        /*
         * function to send strings
         */
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }



        /*
         * receive scores of other players
         */
        public PlayerCE[] receivePlayer() {
            PlayerCE[] pl = new PlayerCE[willJoinPlayers];
            try {
                for(int i=0;i<pl.length;i++){
                    PlayerCE p = (PlayerCE) dIn.readObject();
                    pl[i] = p;
                }
                return pl;

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                e.printStackTrace();
            }
            return pl;
        }

        /*
         * receive scores of other players
         */
        public int[][] receiveScores() {
            try {
                int[][] sc = new int[willJoinPlayers][15];
                for (int j = 0; j < willJoinPlayers; j++) {
                    for (int i = 0; i < 15; i++) {
                        sc[j][i] = dIn.readInt();
                    }
                }

                return sc;
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return null;
        }

        /*
         * receive scores of other players
         */
        public int receiveRoundNo() {
            try {
                return dIn.readInt();

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return 0;
        }

        /*
         * receive PokerStatus of all
         */
        public PokerStatus receivePokerStatus() {
            try {

                return gson.fromJson(dIn.readUTF(),PokerStatus.class);

            } catch (IOException e) {
                System.out.println("PokerStatus not received");
                e.printStackTrace();
            }
            return null;
        }



        public void sendPokerStatus(PokerStatus pokerStatus) {
            try {
                dOut.writeUTF(gson.toJson(pokerStatus));
                dOut.flush();
            } catch (IOException e) {
                System.out.println("pokerStatus not send");
                e.printStackTrace();
            }
        }

        public Poker receivePorkTop() {
            try {
                return gson.fromJson(dIn.readUTF(),Poker.class);
            } catch (IOException e) {
                System.out.println("Top not received");
                e.printStackTrace();
            }
            return null;
        }

        public Object receiveObject(Class<?> clazz) {
            try {
                return gson.fromJson(dIn.readUTF(),clazz);
            } catch (IOException e) {
                System.out.println("Top not received");
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void main(String args[]) {
        Scanner myObj = new Scanner(System.in);

        System.out.print("What is your host ? ");
        String host=myObj.next();
        System.out.print("What is your name ? ");
        String name = myObj.next();
        PlayerCE p = new PlayerCE(name);
        p.initializePlayers();
        p.connectToClient(host);

        while (true) {
            int round = clientConnection.receiveRoundNo();
            if (round == -7){
                p.willJoinPlayers = clientConnection.receiveRoundNo();
            }
            if (round == -6)
                break;
            if (round == -5){
                p.turnNum=p.turnNum+1;
                p.startGame();
                p.returnWinner();
            }
        }

        System.out.println("Game over!");
        int score=500;
        for (int i = 0; i < p.willJoinPlayers; i++) {
            score=score<p.players[i].getScore()?score:p.players[i].getScore();
        }
        for (int i = 0; i < p.willJoinPlayers; i++) {
            if(p.players[i].getScore()==score){
                System.out.println("The winner is "+p.players[i].getName());
            }
        }
        myObj.close();
    }
}
