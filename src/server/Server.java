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
import utilities.ChatServerLogger;

public class Server {

    private static boolean isAlive = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = ChatServerLogger.initProperties( "server.properties" );
    public static List<ClientHandler> listofECH = Collections.synchronizedList( new ArrayList() );
    public static Logger chatLogger;

    public static void main( String[] args ) {
        new Server().startServer();
    }

    public static void stopServer() {
        chatLogger.log( Level.INFO, "Server stopped" );
        ChatServerLogger.closeLogger( Server.class.getName() );
        isAlive = false;
    }

    public void startServer() {
        ClientHandler currentHandler;

        int port = Integer.parseInt( properties.getProperty( "port" ) );
        String ip = properties.getProperty( "serverIp" );
        String logFile = properties.getProperty( "logFile" );

        ChatServerLogger.setLogFile( logFile, Server.class.getName() );
        chatLogger = ChatServerLogger.getLogger( logFile, Server.class.getName() );

        Logger.getLogger( Server.class.getName() ).log( Level.INFO, "Server started" );

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind( new InetSocketAddress( ip, port ) );
            chatLogger.log( Level.INFO, "Chat Server online - " + ip + ":" + port );

            do {
                //Wait for a new client to connect (Important blocking call)
                Socket socket = serverSocket.accept();

                String clientIP = socket.getInetAddress().getHostAddress();
                int clientPort = socket.getPort();

                chatLogger.log( Level.INFO, "Connected to new client client - " + clientIP + ":" + clientPort );
                currentHandler = new ClientHandler( socket, this, chatLogger );
                listofECH.add( currentHandler );
            } while ( isAlive );

        } catch ( IOException ex ) {
            chatLogger.log( Level.SEVERE, "No connection established!", ex );
        }
    }

    public synchronized void removeHandler( ClientHandler clientToBeDeleted ) {
        for ( ClientHandler clientFromList : listofECH ) {
            if ( clientFromList == clientToBeDeleted ) {
                listofECH.remove( clientFromList );
                break;
            }
        }
    }

    public List<ClientHandler> getListofECH() {
        return listofECH;
    }
}
