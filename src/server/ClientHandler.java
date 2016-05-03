package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Protocol;
import utilities.SaveClients;

public class ClientHandler extends Thread {

    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    private Server myServer;
    private String nickname;
    private String listOfHandlerNames;
    private boolean isStopped = false;

    public ClientHandler( Socket socket, Server myServer ) {
        try {
            input = new Scanner( socket.getInputStream() );
            writer = new PrintWriter( socket.getOutputStream(), true );
            this.socket = socket;
            this.myServer = myServer;
        } catch ( IOException ex ) {
            Logger.getLogger( ClientHandler.class.getName() ).log( Level.SEVERE, null, ex );
        }
        start();
    }

    @Override
    public void run() {
        String message = "";
        do {

            message = input.nextLine(); //IMPORTANT blocking call
            if ( message.contains( Protocol.ALL ) ) {
                System.out.println( "Sending to ALL" );
                String[] result = message.split( "#" );

                int size = result.length;
                if ( size == 3 ) {
                    send( Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + result[ 2 ] );
                    System.out.println( "Sending : " + Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + result[ 2 ] );
                } else if ( size == 2 ) {
                    send( Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + result[ 1 ] );
                    System.out.println( "Sending : " + Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + result[ 1 ] );
                }

                Logger.getLogger( Server.class.getName() ).log( Level.INFO, String.format( "Received the message: %1$S ", message ) );

            } else if ( message.contains( Protocol.CONNECT ) ) {
                System.out.println( "Connecting" );
                String[] result = message.split( "#" );
                setNickname( result[ 1 ] ); //Extract Client's nickname
                send(); //List of active users
            } else if ( message.contains( Protocol.NICKNAME ) ) {
                System.out.println( "Nickname" );
                String[] result = message.split( "#" );
                send( Protocol.NICKNAME + result[ 1 ] );
                setNickname( result[ 1 ] );
                send();

            } else if ( message.contains( Protocol.CLOSE ) ) {
                System.out.println( "Closing" );
                for ( ClientHandler handler : myServer.getListofECH() ) {
                    if ( handler == this ) {
                        handler.writer.println( message );
                    }
                }
                isStopped = true; //Go out of do while
            } else { //Whispering
                String[] splitMessageString = message.split( "#" );
                send( splitMessageString );
            }
        } while ( !isStopped );
        if ( message.equals( Protocol.CLOSE ) ) {
            System.out.println( "hello" );
            writer.println( Protocol.CLOSE );//Echo the stop message back to the client for a nice closedown
        }
        try {
            socket.close();
            myServer.removeHandler( this );
            send(); //Refreshing the list of active clients for the others, who are still connected.
        } catch ( IOException ex ) {
            Logger.getLogger( ClientHandler.class.getName() ).log( Level.SEVERE, null, ex );
        }
        Logger.getLogger( Server.class.getName() ).log( Level.INFO, "Closed a Connection" );
    }

    //Generated send message
    private void send( String msg ) {
//        String ip = socket.getInetAddress().getHostAddress();
//        int port = socket.getPort();

        for ( ClientHandler handler : myServer.getListofECH() ) {
            System.out.println( "Sending..." );
            handler.writer.println( msg );
        }
    }

    //List of active users
    private void send() {
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
        SaveClients sc = new SaveClients();
        //sc.actualSaving(listOfHandlerNames);

    }

    //Whispering til en person eller mange mennesker
    private void send( String[] splitMessageString ) {
        int size = myServer.getListofECH().size();
        if ( splitMessageString[ 1 ].contains( "," ) ) { //, means that there are more than 1 names
            String[] listOfClientsToReceiveMessage = splitMessageString[ 1 ].split( "," );
            for ( int i = 0; i < listOfClientsToReceiveMessage.length; i++ ) {
                for ( int j = 0; j < size; j++ ) {
                    if ( myServer.getListofECH().get( j ).getNickname().equals( listOfClientsToReceiveMessage[ i ] ) ) {
                        myServer.getListofECH().get( j ).writer.println( Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + splitMessageString[ 2 ] );
                    }
                }
            }
        } else { //If there is only one name
            for ( int j = 0; j < size; j++ ) {
                if ( myServer.getListofECH().get( j ).getNickname().equals( splitMessageString[ 1 ] ) ) {
                    myServer.getListofECH().get( j ).writer.println( Protocol.MESSAGE + this.getNickname() + Protocol.HashTag + splitMessageString[ 2 ] );
                }
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname( String nickname ) {
        this.nickname = nickname;
    }

}
