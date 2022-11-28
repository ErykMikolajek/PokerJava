package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class Client {
    static InetSocketAddress serverAddress;
    static SocketChannel client;
    static Scanner scanner = new Scanner(System.in);
    static int playerAmount = 100;

    public static void main(String[] args) throws IOException, InterruptedException {
        connectToServer();
        getId();
        while(true) {
            getTextFromServer();
            getTextFromServer();
            getTextFromServer();
            getTextFromServer();
            //getTextFromServer(); // pierwsza tura licytacji
            //betGetBalance();
            //bet();
            getTextFromServer(); // co chcesz zrobic
            sendDecision();
            getTextFromServer(); // Najlepszy układ twoich kart
            getTextFromServer(); // show ranking
            getTextFromServer(); // wygrywa gracz x
            getTextFromServer(); // kolejna tura
        }
        //client.close();
    }

    public static void getId() throws IOException {
        ByteBuffer buffer_response = ByteBuffer.allocate(Integer.BYTES);
        client.read(buffer_response);
        int playerNum = ByteBuffer.wrap(buffer_response.array()).getInt();
        System.out.println("Jesteś graczem numer: " + playerNum);
    }

    public static void connectToServer() throws IOException {
        serverAddress = new InetSocketAddress("localhost", 1234);
        client = SocketChannel.open(serverAddress);

        System.out.println("Połączono z serwerem");
        System.out.println("Aby rozpocząć naciśnij 'r'");

        String messageToSend = scanner.nextLine();
        byte[] message = messageToSend.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        buffer.clear();

        ByteBuffer buffer_response = ByteBuffer.allocate(256);
        client.read(buffer_response);
        String result = new String(buffer_response.array()).trim();
        System.out.println(result);
    }

    public static void getTextFromServer() throws IOException {
        ByteBuffer buffer_response = ByteBuffer.allocate(256);
        client.read(buffer_response);
        String result = new String(buffer_response.array()).trim();
        System.out.println(result);
    }

    public static void betGetBalance() throws IOException {
        ByteBuffer buffer_response = ByteBuffer.allocate(Integer.BYTES);
        client.read(buffer_response);
        playerAmount = ByteBuffer.wrap(buffer_response.array()).getInt();
        buffer_response.clear();
        System.out.println("Twój balans: " + playerAmount);
    }

    public static void bet() throws IOException {
        System.out.println("Wpisz ile chcesz postawic: ");
        int amount = scanner.nextInt();
        while (amount > playerAmount || amount < 0){
            System.out.println("Wpisz poprawną liczbę: ");
            amount = scanner.nextInt();
        }

        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(amount).array();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        client.write(buffer);
        buffer.clear();

        getTextFromServer();
    }

    public static void sendDecision() throws IOException {
        int response = scanner.nextInt();
        String responseToServer = null;
        if (response == 1){
            System.out.println("Podaj bez separatorow numery kart które chcesz wymienić: ");
            int cardsNum = scanner.nextInt();
            responseToServer = "1" + cardsNum;
        }
        else if (response == 2)
            responseToServer = "2";
        else throw new InvalidParameterException("Given response is not valid");

        byte[] message = responseToServer.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        buffer.clear();

        if (response == 1){
            getTextFromServer();
        }

    }
}
