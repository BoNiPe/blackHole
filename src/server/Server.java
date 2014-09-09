package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.Utilities;

public class Server {

    private static boolean isAlive = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utilities.initProperties("server.properties");
    private List<ClientHandler> listofECH = Collections.synchronizedList(new ArrayList());

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        ClientHandler currentHandler;
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        String logFile = properties.getProperty("logFile");
        //Utils.setLogFile(logFile, Server.class.getName());
        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Sever started");
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Connected to a client");
                currentHandler = new ClientHandler(socket, this);
                listofECH.add(currentHandler);
            } while (isAlive);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("SERVER CLOSED");
        //Utilities.closeLogger(EchoServer.class.getName());
    }

    public void stopServer() {
        isAlive = false;
    }
    
        public synchronized void removeHandler(ClientHandler clientToBeDeleted) {
        ClientHandler temp = null;
        for (ClientHandler clientFromList : listofECH) {
            if (clientFromList == clientToBeDeleted) {
                temp = clientFromList;
                break;
            }
        }
        if (temp != null) {
            listofECH.remove(temp);
        }
    }
    
        public List<ClientHandler> getListofECH() {
        return listofECH;
    }
}
