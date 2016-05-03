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
    private static final Properties properties = Utilities.initProperties( "server.properties" );
    public static List<ClientHandler> listofECH = Collections.synchronizedList( new ArrayList() );
    public static Logger chatLogger;

    public static void main( String[] args ) {
        new Server().startServer();
    }

    public void startServer() {
        ClientHandler currentHandler;

        int port = Integer.parseInt( properties.getProperty( "port" ) );
        String ip = properties.getProperty( "serverIp" );
        String logFile = properties.getProperty( "logFile" );

        Utilities.setLogFile( logFile, Server.class.getName() );
        chatLogger = Utilities.getLogger( logFile, Server.class.getName() );
        Logger.getLogger( Server.class.getName() ).log( Level.INFO, "Server started" );
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind( new InetSocketAddress( ip, port ) );
            chatLogger.log( Level.INFO, "Chat Server online, on ip: " + ip + " and port: " + port );
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                chatLogger.log( Level.INFO, "Connected to a client" );
                currentHandler = new ClientHandler( socket, this );
                listofECH.add( currentHandler );
            } while ( isAlive );
        } catch ( IOException ex ) {
            chatLogger.log( Level.SEVERE, "No connection established!", ex );
        }
        System.out.println( "SERVER CLOSED" );
        Utilities.closeLogger( Server.class.getName() );
    }

    public static void stopServer() {
        chatLogger.log( Level.INFO, "Server stopped" );
        isAlive = false;
    }

    public synchronized void removeHandler( ClientHandler clientToBeDeleted ) {
        ClientHandler temp = null;
        for ( ClientHandler clientFromList : listofECH ) {
            if ( clientFromList == clientToBeDeleted ) {
                temp = clientFromList;
                break;
            }
        }
        if ( temp != null ) {
            listofECH.remove( temp );
        }
    }

    public List<ClientHandler> getListofECH() {
        return listofECH;
    }
}
