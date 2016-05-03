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

public class Client extends Thread {

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
        start();
    }

    public void run() {
        String msg = input.nextLine();

        while ( !msg.equals( Protocol.CLOSE ) ) {
            globalMessage.add( msg );
            notifyListeners( msg );
            msg = input.nextLine();
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
        output.println( message );
    }

    public void stopClient() throws IOException {
        output.println( Protocol.CLOSE );
    }

    public void registerEchoListener( EchoListener l ) {
        listeners.add( l );
    }

    public void unRegisterEchoListener( EchoListener l ) {
        listeners.remove( l );
    }

    public boolean isClientConnected( EchoListener l ) {
        for ( EchoListener particularOneFromList : listeners ) {
            if ( l == particularOneFromList ) {
                return true; //connected
            }
        }
        return false; //not connected
    }

    private void notifyListeners( String msg ) {
        for ( EchoListener l : listeners ) {
            l.messageArrived( msg );
        }
    }
}
