package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

public class Server {

    static Player[] players;
    static Deck mainDeck = new Deck();
    static int numOfPlayers = 0;

    static Selector selector;
    static ServerSocketChannel serverSocket;
    static InetSocketAddress serverAddr;
    static Set<SelectionKey> serverKeys;

    public static void main(String[] args) throws IOException {
        System.out.println("Ile graczy będzie grało: ");
        Scanner inputScanner = new Scanner(System.in);
        numOfPlayers = inputScanner.nextInt();

        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverAddr = new InetSocketAddress("localhost", 1234);

        serverSocket.bind(serverAddr);
        serverSocket.configureBlocking(false);

        int ops = serverSocket.validOps();
        SelectionKey selectKey = serverSocket.register(selector, ops, null);

        // gra
        SelectionKey[] temp = selector.keys().toArray(new SelectionKey[0]);
        SelectionKey server_key = temp[0];

        connectPlayers();
        players = new Player[numOfPlayers];
        temp = selector.keys().toArray(new SelectionKey[0]);
        SelectionKey[] keysArray = new SelectionKey[temp.length - 1];
        int j = 0;
        for (SelectionKey selectionKey : temp) {
            if (!selectionKey.equals(server_key))
                keysArray[j++] = selectionKey;
        }
        for (int i = 0; i < numOfPlayers; i++){
            Card[] cardsForPlayer = mainDeck.dealCards(5);
            players[i] = new Player(cardsForPlayer, keysArray[i]);
        }
        sendToAll("Gra rozpoczęta z " + numOfPlayers + " graczami.");
        identifyPlayers();
        //while(true) {
            sendToAll("Twoje karty:");
            showCards();
            sendToAll("Najlepszy układ twoich kart: ");
            showRanking();
            //sendToAll("Pierwsza tura licytacji");
            //bettingInfo();
            //bettingResponse();
            sendToAll("Wybierz, co chcesz zrobic: 1. Wymienic karty, 2. Spasowac");
            getDecision();
            for (Player p : players) {
                if (p.lastDecision != null || p.lastDecision.charAt(0) == '1'){
                    for (int i = 1; i < p.lastDecision.length(); i++){
                        int index = p.lastDecision.charAt(i);
                        p.cards_[index] = mainDeck.dealCards(1)[0];
                    }
                    for (SelectionKey key : serverKeys) {
                        if (key.isReadable() && key.equals(p.playerKey)) {
                            SocketChannel client = (SocketChannel) key.channel();
                            String cardsToSend = p.show_cards();
                            byte[] messageInBytes = cardsToSend.getBytes();
                            ByteBuffer bufferToSend = ByteBuffer.wrap(messageInBytes);
                            client.write(bufferToSend);
                            client.register(selector, SelectionKey.OP_READ);
                        }
                    }
                }
            }

            Player winningPlayer = players[0];
            for (Player p : players) {
                if (p.handRankingGreaterThan(winningPlayer)) {
                    winningPlayer = p;
                }
            }
            for (int i = 0; i < players.length; i++) {
                if (players[i] == winningPlayer) {
                    sendToAll("Wygrywa gracz " + (i + 1));
                }
            }
        //}

    }

    public static void getDecision() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                int playerNum = -1;
                for (int i = 0; i < players.length; i++)
                    if (players[i].playerKey.equals(key)){
                        playerNum = i + 1;
                    }

                ByteBuffer buffer = ByteBuffer.allocate(256);
                client.read(buffer);

                players[playerNum-1].lastDecision = new String(buffer.array()).trim();
            }
        }
    }

    public static void identifyPlayers() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                int playerNum = -1;
                for (int i = 0; i < players.length; i++)
                    if (players[i].playerKey.equals(key)){
                        playerNum = i + 1;
                    }
                byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(playerNum).array();
                ByteBuffer bufferToSend = ByteBuffer.wrap(bytes);
                client.write(bufferToSend);
                bufferToSend.clear();
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    public static void showCards() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                String cards = null;
                for (Player p : players)
                    if (p.playerKey.equals(key))
                        cards = p.show_cards();
                assert cards != null;
                byte[] messageInBytes = cards.getBytes();
                ByteBuffer bufferToSend = ByteBuffer.wrap(messageInBytes);
                client.write(bufferToSend);
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    public static void bettingResponse() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                int currentPlayerIndex = -1;
                for (int i = 0; i < players.length; i++)
                    if (players[i].playerKey.equals(key)){
                        currentPlayerIndex = i;
                    }

                ByteBuffer buffer_response = ByteBuffer.allocate(Integer.BYTES);
                client.read(buffer_response);
                int playerBet = ByteBuffer.wrap(buffer_response.array()).getInt();
                players[currentPlayerIndex].balance -= playerBet;
                //sendToAll("Gracz " + (currentPlayerIndex + 1) + " postawił " + playerBet);
            }
        }
    }

    public static void bettingInfo() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                int money = 0, currentPlayerIndex = -1;
                for (int i = 0; i < players.length; i++)
                    if (players[i].playerKey.equals(key)){
                        money = players[i].balance;
                        currentPlayerIndex = i;
                    }

                byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(money).array();
                ByteBuffer bufferToSend = ByteBuffer.wrap(bytes);
                client.write(bufferToSend);
                bufferToSend.clear();
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    public static void showRanking() throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                String ranking = null;
                for (Player p : players)
                    if (p.playerKey.equals(key))
                        ranking = "" + p.whatHandRanking();
                assert ranking != null;
                byte[] messageInBytes = ranking.getBytes();
                ByteBuffer bufferToSend = ByteBuffer.wrap(messageInBytes);
                client.write(bufferToSend);
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    public static void connectPlayers() throws IOException {
        int readyPlayers = 0;
        while (readyPlayers != numOfPlayers || numOfPlayers == 0) {
            System.out.println("Czekam na połączenie...");

            selector.select();

            serverKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = serverKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    // Połączenie zaakceptowane przez ServerSocketChannel
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Połączenie zaakceptowane, od " + client);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String result = new String(buffer.array()).trim();

                    System.out.println("Gracz " + client + " gotowy.");

                    if (result.equals("r") || result.equals("R"))
                        readyPlayers++;
                }

                iter.remove();
            }
        }
    }

    public static void sendToAll(String message) throws IOException {
        serverKeys = selector.keys();
        for (SelectionKey key : serverKeys) {
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                client.register(selector, SelectionKey.OP_WRITE);
                byte[] messageInBytes = message.getBytes();
                ByteBuffer bufferToSend = ByteBuffer.wrap(messageInBytes);
                client.write(bufferToSend);
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

}
