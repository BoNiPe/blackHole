package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Protocol;

public class ClientHandler extends Thread {

    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    private Server myServer;
    private String nickname;
    private String listOfHandlerNames;
    private boolean isStopped = false;

    public ClientHandler(Socket socket, Server myServer) {
        try {
            input = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.socket = socket;
            this.myServer = myServer;
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    @Override
    public void run() {
        String message = "";
        do {

            message = input.nextLine(); //IMPORTANT blocking call

            if (message.contains(Protocol.NICKNAME)) {

                String[] result = message.split("#");
                send(Protocol.NICKNAME + result[1]);
                setNickname(result[1]);
                send();

            } else if (message.contains(Protocol.SEND)) {

                String[] result = message.split("#");
                send(Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + result[2]);
                Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));

            } else if (message.contains(Protocol.CONNECT)) {

                String[] result = message.split("#");
                setNickname(result[1]);
                send();

            } else if (message.contains(Protocol.CLOSE)) {
                isStopped = true;
            }

        } while (!isStopped);

        writer.println(Protocol.CLOSE);//Echo the stop message back to the client for a nice closedown
        try {
            socket.close();
            myServer.removeHandler(this);
            send();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    //Generated send message
    private void send(String msg) {
        String ip = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        //sout " (" + ip + ":" + port + ") :"
        //getNickname() + " " +  (additional stuff)

        for (ClientHandler handler : myServer.getListofECH()) {
            handler.writer.println(msg);
        }
    }

    //List of active users
    private void send() {
        listOfHandlerNames = "";

        Integer ammountOfClients = myServer.getListofECH().size();
        for (int i = 0; i < ammountOfClients; i++) {
            if (i < ammountOfClients - 1) {
                listOfHandlerNames += myServer.getListofECH().get(i).getNickname() + ",";
            } else {
                listOfHandlerNames += myServer.getListofECH().get(i).getNickname();
            }

        }
        for (int i = 0; i < ammountOfClients; i++) {
            myServer.getListofECH().get(i).writer.println(Protocol.ONLINE + listOfHandlerNames);
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
