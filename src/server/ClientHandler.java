package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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

    private Logger chatLogger;

    public ClientHandler( Socket socket, Server myServer, Logger chatLogger ) {
        try {
            input = new Scanner( socket.getInputStream() );
            writer = new PrintWriter( socket.getOutputStream(), true );
            this.socket = socket;
            this.myServer = myServer;
            this.chatLogger = chatLogger;
        } catch ( IOException ex ) {
            Logger.getLogger( ClientHandler.class.getName() ).log( Level.SEVERE, null, ex );
        }
        start();
    }

    @Override
    public void run() {
        String message = "";
        do {
            message = input.nextLine(); //important blocking call
            chatLogger.info( "Received message from user " + getNickname() + ": " + message );
            String[] messageParts = message.split( "#" );

            switch ( messageParts[ 0 ] + "#" ) { //Follow protocol string syntax
                case Protocol.CONNECT:

                    setNickname( messageParts[ 1 ] );
                    sendListOfClients();
                    chatLogger.info( "User " + getNickname() + " connected successfully" );

                    break;
                case Protocol.SEND:

                    if ( Protocol.ALL.equals( messageParts[ 1 ] + "#" ) ) {
                        sendPublicMessage( Protocol.MESSAGE, this.getNickname()
                                           + Protocol.HashTag + messageParts[ 2 ] );
                    } else {
                        sendPrivateMessage( messageParts[ 1 ], Protocol.PRIVATEMESSAGE
                                            + this.getNickname() + Protocol.HashTag
                                            + messageParts[ 2 ] );
                    }

                    chatLogger.info( "User " + getNickname() + " send message to " + messageParts[ 1 ] + " : " + messageParts[ 2 ] );
                    break;
                case Protocol.NICKNAME:

                    String oldNickname = getNickname();
                    setNickname( messageParts[ 1 ] );

                    sendPublicMessage( "NAMECHANGE#", oldNickname + "#BECAME#" + getNickname() );
                    sendListOfClients();
                    chatLogger.info( "User " + oldNickname + " changed name to " + getNickname() );
                    break;
                default:
                    chatLogger.warning( "Incorrect message or close request, User : " + getNickname() );
                    isStopped = true;
                    break;
            }
        } while ( !isStopped );

        for ( ClientHandler handler : myServer.getListofECH() ) {
            if ( handler == this ) {
                handler.writer.println( message );
            }
        }

        try {

            socket.close();
            myServer.removeHandler( this );
            sendPublicMessage( "DISCONNECT#", getNickname() );
            sendListOfClients();

        } catch ( IOException ex ) {
            chatLogger.severe( "IOException while trying to close the socket : " + ex.getMessage() );
        }
        chatLogger.info( "Connection with " + getNickname() + " closed." );
    }

    private void sendPublicMessage( String type, String msg ) {
        for ( ClientHandler handler : myServer.getListofECH() ) {
            handler.writer.println( type + msg );
        }
    }

    private void sendPrivateMessage( String userName, String message ) {
        for ( ClientHandler handler : myServer.getListofECH() ) {
            if ( userName.equals( handler.getNickname() ) ) {
                handler.writer.println( message );
            }
        }
    }

    private void sendListOfClients() {
        listOfHandlerNames = "";
        Integer ammountOfClients = myServer.getListofECH().size();
        for ( int i = 0; i < ammountOfClients; i++ ) {
            if ( i < ammountOfClients - 1 ) {
                listOfHandlerNames += myServer.getListofECH().get( i ).getNickname() + ",";
            } else {
                listOfHandlerNames += myServer.getListofECH().get( i ).getNickname();
            }
        }
        for ( int i = 0; i < ammountOfClients; i++ ) {
            myServer.getListofECH().get( i ).writer.println( Protocol.ONLINE + listOfHandlerNames );
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname( String nickname ) {
        this.nickname = nickname;
    }

}
