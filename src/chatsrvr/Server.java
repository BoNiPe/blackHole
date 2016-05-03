package chatsrvr;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import utilities.Utilities;

public class Server
{

    private static boolean isAlive = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utilities.initProperties("chatServerConfig.properties");
    private List<ClientHandler> listofClients = Collections.synchronizedList(new ArrayList());

    public static void main(String[] args)
    {
        new Server().startServer(); //Starting the server and removing the need of 'Static' variables.
    }

    public void startServer()
    {
        ClientHandler currentHandler;
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        String logFile = properties.getProperty("logFile");
        Utilities.setLogFile(logFile, Server.class.getName());
        java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Server started");
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.INFO, ">IP: " + ip + ":" + port);
            do
            {
                Socket socket = serverSocket.accept(); //Important Blocking call
                java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Connected to a client");
                currentHandler = new ClientHandler(socket, this);
                listofClients.add(currentHandler);
            } while (isAlive);
        }
        catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        Utilities.closeLogger(Server.class.getName());
    }

    public static void stopServer()
    {
        java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Server stopped");
        isAlive = false;
    }

    public synchronized void removeHandler(ClientHandler clientToBeDeleted)
    {
        ClientHandler temp = null;
        for (ClientHandler clientFromList : listofClients)
        {
            if (clientFromList == clientToBeDeleted)
            {
                temp = clientFromList;
                break;
            }
        }
        if (temp != null)
        {
            listofClients.remove(temp);
        }
    }

    public List<ClientHandler> getListofClients()
    {
        return listofClients;
    }
}
