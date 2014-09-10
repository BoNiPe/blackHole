package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    private List<String> listOfNicknames = new ArrayList();

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
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(Protocol.CLOSE)) {

            if (message.contains(Protocol.NICKNAME)) {
                String[] result = message.split("#");
                System.out.println("Nickname.");
                if (result[1].length() <= 8) {
                    send(Protocol.NICKNAME + result[1]);
                    setNickname(result[1]);
                } else {
                    send("Nickname is not suitable!");
                }
            } else if (message.contains(Protocol.MESSAGE)) {
                System.out.println("Simple message.");
                send(message);
                Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
            } else if (message.contains(Protocol.CONNECT)) {
                System.out.println("Connect.");
                String[] result = message.split("#");
                setNickname(result[1]);
                send(Protocol.CONNECT + getNickname());
                listOfNicknames.add(getNickname());
                send(Protocol.ONLINE + listOfNicknames);
            }
            message = input.nextLine(); //IMPORTANT blocking call
        }
        if (message.equals(Protocol.CLOSE)) {
            send(Protocol.CLOSE);
            writer.println(Protocol.CLOSE);//Echo the stop message back to the client for a nice closedown
        }
        try {
            socket.close();
            myServer.removeHandler(this);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    private void send(String msg) {
        String ip = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();

        for (ClientHandler handler : myServer.getListofECH()) {
            handler.writer.println(getNickname() + " (" + ip + ":" + port + ") :" + msg);
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getListOfNicknames() {
        return listOfNicknames;
    }
}
