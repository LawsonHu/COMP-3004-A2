package com.a2.crazyEightGame;

import com.a2.crazyEightGame.constant.CommonNumVar;
import com.a2.crazyEightGame.entity.*;
import gherkin.deps.com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameCEServer implements Serializable {
    private static final long serialVersionUID = 4772231289249135460L;


    int numPlayers;
    private int turnsMade;
    private int maxTurns;
    private boolean gameover;

    private int willJoinPlayers;

    private PlayerCE[] players;
    Server[] playerServer;
    ServerSocket ss;
    GameCE game=new GameCE();

    public GameCEServer(){
        System.out.println("Starting game server");
        System.out.println("Please input players number: 3 & 4");
        Scanner sc=new Scanner(System.in);
        willJoinPlayers=sc.nextInt();
        players =new PlayerCE[willJoinPlayers];
        playerServer = new Server[willJoinPlayers];

        numPlayers = 0;
        gameover=false;
        // initialize the players list with new players
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayerCE();
        }

        try {
            ss = new ServerSocket(3333);
        } catch (IOException ex) {
            System.out.println("Server Failed to open");
        }
    }

    public boolean isGameover() {
        for(int i=0;i<players.length;i++){
            if(players[i].getScore()>=100){
                return true;
            }
        }
        return false;
    }

    public class Server implements Runnable {
        private Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        private int playerId;
        private Gson gson = new Gson();

        public Server(Socket s, int playerid) {
            socket = s;
            playerId = playerid;
            try {
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Server Connection failed");
            }
        }

        /*
         * run function for threads --> main body of the thread will start here
         */
        public void run() {
            try {
                while (true) {
                }

            } catch (Exception ex) {
                {
                    System.out.println("Run failed");
                    ex.printStackTrace();
                }
            }
        }
        /*
         * send the scores to other players
         */
        public void sendPlayers(PlayerCE[] pl) {
            try {
                for (PlayerCE p : pl) {
                    dOut.writeObject(p);
                    dOut.flush();
                }

            } catch (IOException ex) {
                System.out.println("Score sheet not sent");
                ex.printStackTrace();
            }

        }

        /*
         * receive scores of other players
         */
        public void sendTurnNo(int r) {
            try {
                dOut.writeInt(r);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
        }

        /*
         * receive scores of other players
         */
        public int[] receiveScores() {
            try {
                int[] sc = new int[15];
                for (int i = 0; i < 15; i++) {
                    sc[i] = dIn.readInt();
                }
                return sc;
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return null;
        }

        /*
         * send scores of other players
         */
        public void sendScores(PlayerCE[] pl) {
            try {
                for (int i = 0; i < pl.length; i++) {
                    for (int j = 0; j < pl[i].getScoreSheet().length; j++) {
                        dOut.writeInt(pl[i].getScoreSheet()[j]);
                    }
                }
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Score sheet not sent");
                e.printStackTrace();
            }
        }



        public void sendPokerStatus(PokerStatus pokerStatus) {
            try {
                String fieldstr = gson.toJson(pokerStatus);
                dOut.writeUTF(fieldstr);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("PokerTop not sent");
                e.printStackTrace();
            }
        }



        /*
         * receive Poker of other players
         */

        public PokerStatus receivePokerStatus() {
            try {
                return gson.fromJson(dIn.readUTF(),PokerStatus.class);
            } catch (Exception e) {
                System.out.println("Poker not received");
                e.printStackTrace();
            }
            return null;
        }

        public void sendPokerTop(Poker top) {
            try {
                String fieldstr = gson.toJson(top);
                dOut.writeUTF(fieldstr);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Top not sent");
                e.printStackTrace();
            }
        }

        public void sendObject(Object currentPoker) {
            try {
                String fieldstr = gson.toJson(currentPoker);
                dOut.writeUTF(fieldstr);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Top not sent");
                e.printStackTrace();
            }
        }
    }

    /*
     * -----------Networking stuff ----------
     *
     */
    public void acceptConnections() throws ClassNotFoundException {
        try {
            System.out.println("Waiting for players...");
            while (numPlayers < willJoinPlayers) {
                Socket s = ss.accept();
                numPlayers++;

                Server server = new Server(s, numPlayers);

                // send the player number
                server.dOut.writeInt(server.playerId);
                server.dOut.flush();

                // get the player name
                PlayerCE in = (PlayerCE) server.dIn.readObject();
                System.out.println("Player " + server.playerId + " ~ " + in.getName() + " ~ has joined");
                // add the player to the player list
                players[server.playerId - 1] = in;
                playerServer[numPlayers - 1] = server;
            }
            System.out.println(willJoinPlayers+" players have joined the game");

            // start the server threads
            for (int i = 0; i < playerServer.length; i++) {
                Thread t = new Thread(playerServer[i]);
                t.start();
            }
            // start their threads
        } catch (IOException ex) {
            System.out.println("Could not connect "+willJoinPlayers+" players");
        }
    }

    public void gameLoop(int k) {
        try {
            for (int i = 0; i < playerServer.length; i++) {
                playerServer[i].sendTurnNo(-5);
            }
            for (int i = 0; i < playerServer.length; i++) {
                playerServer[i].sendPlayers(players);
            }

            GameCE gameCE=new GameCE();

            PokerDui prePoker = gameCE.preGame();

            PokerStatus playerPockerStatus =null;
            for (int i = 0; i < players.length; i++) {
                players[i].setPokerHand(null);
            }

            boolean shun=true;

            tag3:
            while (turnsMade < maxTurns) {

                turnsMade++;

                // send the round number
                System.out.println("*****************************************");
                System.out.println("Round number " + turnsMade);

                tag2:
                for (int i = 0; i < playerServer.length; ) {
                    playerServer[i].sendTurnNo(turnsMade);
                    playerServer[i].sendScores(players);
                    if (players[i].getPokerHand() == null) {
                        players[i].setPokerHand(prePoker.pop(5));
                    }
                    //Five cards for each
                    //Show a card
                    if (null == playerPockerStatus) {
                        Poker top = prePoker.popTop();
                        tag1:
                        for (int j = 0; j < playerServer.length; j++) {
                            if(i==j){ continue tag1; }
                            playerServer[j].sendTurnNo(-4);
                            playerServer[j].sendPokerTop(top);
                        }
                        playerPockerStatus=new PokerStatus(top, players[i].getPokerHand(), prePoker);
                        playerServer[i].sendPokerStatus(playerPockerStatus);
                    } else {
                        playerPockerStatus.setPokerHand(players[i].getPokerHand());
                        playerPockerStatus.setPokerDui(prePoker);
                        playerPockerStatus.addOldTopPoker(playerPockerStatus.getPokerTop());
                        playerServer[i].sendPokerStatus(playerPockerStatus);
                    }

                    String temp=new String(playerPockerStatus.getPokerTop().getNumber()+playerPockerStatus.getPokerTop().getType());
                    int dui=playerPockerStatus.getPokerDui().getPokers().size();
                    tag4:
                    for (int j = 0; j < playerServer.length; j++) {
                        if(i==j){ continue tag4; }
                        playerServer[j].sendTurnNo(-2);
                        playerServer[j].sendObject(
                                    new String("This is Player " + (i + 1) + " " + players[i].getName() +
                                            " round. Please wait to play... "));

                    }
                    playerPockerStatus = playerServer[i].receivePokerStatus();
                    tag5:
                    for (int j = 0; j < playerServer.length; j++) {
                        if(i==j){ continue tag5; }
                        playerServer[j].sendTurnNo(-3);
                        if(new String(playerPockerStatus.getPokerTop().getNumber()+playerPockerStatus.getPokerTop().getType()).equals(temp)){
                            playerServer[j].sendObject(
                                    new CurrentPoker("LastPoker size is " + playerPockerStatus.getPokerDui().getPokers().size()+",Player " + (i + 1) + " " + players[i].getName() + " hasn't play any poker! Current poker is ",
                                            playerPockerStatus.getPokerTop()));
                        }else {
                            playerServer[j].sendObject(
                                    new CurrentPoker("LastPoker size is " + playerPockerStatus.getPokerDui().getPokers().size()+",Player " + (i + 1) + " " + players[i].getName() + " has play ",
                                            playerPockerStatus.getPokerTop()));
                        }
                    }
                    prePoker = playerPockerStatus.getPokerDui();
                    players[i].setPokerHand(playerPockerStatus.getPokerHand());
                    System.out.println(playerPockerStatus.getPokerHand().getPokers());
                    players[i].getScoreSheet()[k] = playerPockerStatus.getPokerHand().getScores();
                    players[i].setScoreSheet(players[i].getScoreSheet());
                    System.out.println("Player "+(i+1)+" completed turn and their score is " + players[i].getScoreSheet()[k]);
                    if (playerPockerStatus.gameover()) {
                        break tag3;
                    }
                    System.out.println("LastPoker size is " + playerPockerStatus.getPokerDui().getPokers().size());

                    if(new String(playerPockerStatus.getPokerTop().getNumber()+playerPockerStatus.getPokerTop().getType()).equals(temp)){
                        if(shun){
                            i++;
                        }else {
                            if(i==0){
                                i=playerServer.length-1;
                            }else if(i==1){
                                break tag2;
                            }else {
                                i--;
                            }
                        }
                    }else {
                        if(playerPockerStatus.getPokerTop().getNumber().equals(CommonNumVar.A.getNumber())){
                            for (int j = 0; j < playerServer.length; j++) {
                                playerServer[j].sendTurnNo(-2);
                                playerServer[j].sendObject("Reverses the direction of play!");
                            }
                            if(shun)
                                shun=false;
                            else
                                shun=true;
                        }
                        if(shun){
                            i++;
                        }else {
                            if(i==0){
                                i=playerServer.length-1;
                            }else if(i==1){
                                break tag2;
                            }else {
                                i--;
                            }
                        }
                        if(playerPockerStatus.getPokerTop().getNumber().equals(CommonNumVar.Q.getNumber())){

                            if(shun){
                                for (int j = 0; j < playerServer.length; j++) {
                                    playerServer[j].sendTurnNo(-2);
                                    playerServer[j].sendObject("The next player "+(i==playerServer.length?1:(i+1))+" will miss their turn!");
                                }
                                if(i==playerServer.length){
                                    i=1;
                                }else {
                                    i+=1;
                                }
                            }else {
                                for (int j = 0; j < playerServer.length; j++) {
                                    playerServer[j].sendTurnNo(-2);
                                    playerServer[j].sendObject("The next player "+(i==(playerServer.length-1)?1:(i-1))+" will miss their turn!");
                                }
                                if(i==0){
                                    i=playerServer.length-1;
                                }else {
                                    i-=1;
                                }
                            }
                        }
                    }
                }

            }


            PlayerCE p = game.getWinner(players);
            System.out.println("The winner is " + p.getName());
            for (int i = 0; i < playerServer.length; i++) {
                playerServer[i].sendTurnNo(-1);
                playerServer[i].sendScores(players);
                playerServer[i].dOut.writeObject(p);
                playerServer[i].dOut.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws Exception {
        GameCEServer sr = new GameCEServer();

        sr.acceptConnections();
        for (int i = 0; i < sr.playerServer.length; i++) {
            sr.playerServer[i].sendTurnNo(-7);
            sr.playerServer[i].sendTurnNo(sr.willJoinPlayers);
        }
        int i=0;
        while (true){
            sr.turnsMade = 0;
            sr.maxTurns = 41;
            if(sr.isGameover()){
                break;
            }
            for (int k = 0; k < sr.players.length; k++) {
                sr.players[k].setTurnNum(i);
            }
            sr.gameLoop(i);
            i++;
        }
        /**
         * test only 1 turn
        sr.turnsMade = 0;
        sr.maxTurns = 41;
        sr.gameLoop(i);
        */
        for (int k = 0; k < sr.playerServer.length; k++) {
            sr.playerServer[k].sendTurnNo(-6);
        }
        System.exit(0);
    }

}
