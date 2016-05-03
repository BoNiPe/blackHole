
import client.Client;
import client.EchoListener;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import server.Server;

public class TestClients {

    public TestClients() {
    }

    String msg;
    String msg2;
    String msg3;
    String msg4;
    Client boyko = new Client();
    Client nik = new Client();
    Client peter = new Client();
    Client superman = new Client();

    @BeforeClass
    public static void setUpClass() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                //Check if actual server is running, if error
                Server.main( null );
            }
        } ).start();

    }

    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {

        //------------CONNECTING THE 3 CLIENTS--------------------
        boyko.connect( "localhost", 9090 );
        boyko.send( "CONNECT#Boyko" );
        Thread.sleep( 100 );

        nik.connect( "localhost", 9090 );
        nik.send( "CONNECT#Nik" );
        Thread.sleep( 100 );

        peter.connect( "localhost", 9090 );
        peter.send( "CONNECT#Peter" );
        Thread.sleep( 100 );
    }

    @After
    public void tearDown() throws InterruptedException {
        boyko.send( "CLOSE#" );
        Thread.sleep( 100 );
        nik.send( "CLOSE#" );
        Thread.sleep( 100 );
        peter.send( "CLOSE#" );
        Thread.sleep( 100 );
        superman.send( "CLOSE#" );
        Thread.sleep( 100 );
    }

    //-----------------CONNECT-----------------
    @Test
    public void connect() throws IOException, InterruptedException {

        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg = data;
            }
        };
        superman.registerEchoListener( listen4 );
        superman.connect( "localhost", 9090 );
        superman.send( "CONNECT#Superman" );
        Thread.sleep( 100 );

        assertEquals( "ONLINE#Boyko,Nik,Peter,Superman", msg );
    }

    //-----------------SEND-----------------
    @Test
    public void sendPrivate() throws IOException, InterruptedException {
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg = data;
            }
        };

        superman.registerEchoListener( listen4 );
        superman.connect( "localhost", 9090 );
        superman.send( "CONNECT#Superman" );
        Thread.sleep( 100 );

        boyko.send( "SEND#Superman#Wazap" );
        Thread.sleep( 100 );
        assertEquals( "PRIVATEMESSAGE#Boyko#Wazap", msg );

    }

    @Test
    public void sendToAll() throws IOException, InterruptedException {
        //Setting up listeners to test who recieves the message
        EchoListener listen1 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg = data;
            }
        };
        boyko.registerEchoListener( listen1 );

        EchoListener listen2 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg2 = data;
            }
        };
        nik.registerEchoListener( listen2 );

        EchoListener listen3 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg3 = data;
            }
        };
        peter.registerEchoListener( listen3 );

        //Connecting the Superman
        superman.connect( "localhost", 9090 );
        superman.send( "CONNECT#Superman" );
        Thread.sleep( 10 );

        //Sending the message to Boyko and Nik
        superman.send( "SEND#*#Hey dudes" );

        Thread.sleep( 100 );
        assertEquals( "MESSAGE#Superman#Hey dudes", msg );
        assertEquals( "MESSAGE#Superman#Hey dudes", msg2 );
        assertEquals( "MESSAGE#Superman#Hey dudes", msg3 );
    }

    @Test
    public void sendNonsense() throws IOException, InterruptedException {
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg = data;
            }
        };

        superman.registerEchoListener( listen4 );
        superman.connect( "localhost", 9090 );
        superman.send( "CONNECT#Superman" );
        Thread.sleep( 100 );
        msg = "hej";
        superman.send( "lolfag" );
        Thread.sleep( 100 );

    }

    //-----------------CLOSE-----------------
    @Test
    public void close() throws InterruptedException, IOException {
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived( String data ) {
                msg = data;
            }
        };

        superman.registerEchoListener( listen4 );
        superman.connect( "localhost", 9090 );
        superman.send( "CONNECT#Superman" );
        Thread.sleep( 100 );
        msg = "lol";
        superman.send( "CLOSE#" );

        Thread.sleep( 100 );
        assertEquals( "CLOSE#", msg );

    }
}
