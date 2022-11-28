package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class Client {
    static InetSocketAddress serverAddress;
    static SocketChannel thisClient;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        connectToServer();
        getId();


        while(true) {
            ByteBuffer buffer_response = ByteBuffer.allocate(256);
            thisClient.read(buffer_response);
            String result = new String(buffer_response.array()).trim();

            if (result.contains("Wybierz, co chcesz zrobic")) {
                System.out.println("Wybierz, co chcesz zrobic: 1. Wymienic karty, 2. Spasowac");
                if (sendDecision() == 0) continue;
            }

            System.out.println(result);
        }
    }

    public static void getId() throws IOException {
        ByteBuffer buffer_response = ByteBuffer.allocate(Integer.BYTES);
        thisClient.read(buffer_response);
        int playerNum = ByteBuffer.wrap(buffer_response.array()).getInt();
        System.out.println("Jesteś graczem numer: " + playerNum);
    }

    public static void connectToServer() throws IOException {
        serverAddress = new InetSocketAddress("localhost", 1235);
        thisClient = SocketChannel.open(serverAddress);

        System.out.println("Połączono z serwerem");
        System.out.println("Aby rozpocząć naciśnij 'r'");

        String messageToSend = scanner.nextLine();
        byte[] message = messageToSend.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        thisClient.write(buffer);
        buffer.clear();

        ByteBuffer buffer_response = ByteBuffer.allocate(256);
        thisClient.read(buffer_response);
        String result = new String(buffer_response.array()).trim();
        System.out.println(result);
    }

    public static void getTextFromServer() throws IOException {
        ByteBuffer buffer_response = ByteBuffer.allocate(256);
        thisClient.read(buffer_response);
        String result = new String(buffer_response.array()).trim();
        System.out.println(result);
    }

    public static int sendDecision() throws IOException {
        int response = scanner.nextInt();
        String responseToServer;
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
        thisClient.write(buffer);
        buffer.clear();

        if (response == 1){
            getTextFromServer();
        }
        return 0;
    }
}
