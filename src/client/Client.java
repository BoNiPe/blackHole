package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Protocol;

public class Client extends Thread implements EchoListener {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    List<EchoListener> listeners = new ArrayList();
    public List<String> globalMessage = new ArrayList();

    public void connect( String address, int port ) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName( address );
        socket = new Socket( serverAddress, port );
        input = new Scanner( socket.getInputStream() );
        output = new PrintWriter( socket.getOutputStream(), true );  //Set to true, to get auto flush behaviour
        System.out.println( getState() );
        start();
    }

    public void run() {
        String msg = input.nextLine();
        System.out.println( "Clients input is : " + msg );
        while ( !msg.equals( Protocol.CLOSE ) ) {
            globalMessage.add( msg );
            notifyListeners( msg );
            msg = input.nextLine();
            messageArrived( msg );
        }
        try {

            globalMessage.add( msg );
            notifyListeners( msg );
            socket.close();
            socket = null;
            this.interrupt();
        } catch ( IOException ex ) {
            Logger.getLogger( Client.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public void send( String message ) {
        System.out.println( "Sending:" );
        if ( message.contains( Protocol.SEND ) ) {
            System.out.println( message );
        }
        output.println( message );
    }

    public void stopClient() throws IOException {
        System.out.println( "Stopping client" );
        output.println( Protocol.CLOSE );
    }

    public void registerEchoListener( EchoListener l ) {
        System.out.println( "Register listener" );
        listeners.add( l );
    }

    public void unRegisterEchoListener( EchoListener l ) {
        System.out.println( "Unregister listener" );
        listeners.remove( l );
    }

    public boolean isClientConnected( EchoListener l ) {
        System.out.println( "Is client connected" );
        for ( EchoListener particularOneFromList : listeners ) {
            if ( l == particularOneFromList ) {
                return true; //connected
            }
        }
        return false; //not connected
    }

    private void notifyListeners( String msg ) {
        System.out.println( "Notify listeners" );
        for ( EchoListener l : listeners ) {
            l.messageArrived( msg );
        }
    }

    @Override
    public void messageArrived( String data ) {
        System.out.println( "messageArrived(String data) (Client): " + data );
    }
}
