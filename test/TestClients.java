/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author Desting
 */
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.main(null);
            }
        }).start();

    }

    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {

        //------------CONNECTING THE 3 CLIENTS--------------------
        System.out.println("SETUP");
        boyko.connect("localhost", 9090);
        boyko.send("CONNECT#Boyko");
        Thread.sleep(100);

        nik.connect("localhost", 9090);
        nik.send("CONNECT#Nik");
        Thread.sleep(100);

        peter.connect("localhost", 9090);
        peter.send("CONNECT#Peter");
        Thread.sleep(100);
        System.out.println("SETUP DONE");
    }
    
    @After
    public void tearDown() throws InterruptedException {
        System.out.println("TEARDOWN");
        boyko.send("CLOSE#");
        Thread.sleep(100);
        nik.send("CLOSE#");
        Thread.sleep(100);
        peter.send("CLOSE#");
        Thread.sleep(100);
        superman.send("CLOSE#");
        Thread.sleep(100);
        System.out.println("TEARDOWN DONE");
    }
    

    //-----------------CONNECT-----------------
    @Test
    public void connect() throws IOException, InterruptedException {
        System.out.println("STARTING FIRST TEST: ");
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println("Superman wants to connect: "+data);
                msg = data;
            }
        };
        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(100);

        assertEquals("ONLINE#Boyko,Nik,Peter,Superman", msg);
        System.out.println("first test done");
    }

    //-----------------SEND-----------------
    @Test
    public void sendPrivate() throws IOException, InterruptedException {
System.out.println("STARTING SECOND TEST: ");
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println("Private message from Boyko to Superman: "+data);
                msg = data;
            }
        };

        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(100);

        boyko.send("SEND#Superman#Wazap");
        Thread.sleep(100);
        assertEquals("MESSAGE#Boyko#Wazap", msg);
        System.out.println("second test done");

    }

    @Test
    public void sendToSeveral() throws IOException, InterruptedException {
        //Setting up listeners to test who recieves the message
        System.out.println("STARTING THIRD TEST: ");
        EchoListener listen1 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println("Message from Superman to Boyko and Nik"+data);
                msg=data;
            }
        };
        boyko.registerEchoListener(listen1);
        
        EchoListener listen2 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg2=data;
            }
        };
        nik.registerEchoListener(listen2);
        
        EchoListener listen3 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg3=data;
            }
        };
        peter.registerEchoListener(listen3);
        
        //Connecting the Superman
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);
        
        //Sending the message to Boyko and Nik
        superman.send("SEND#Boyko,Nik#Hey dudes");
        
        Thread.sleep(100);
        assertEquals("MESSAGE#Superman#Hey dudes", msg);
        assertEquals("MESSAGE#Superman#Hey dudes", msg2);
        assertFalse("MESSAGE#Superman#Hey dudes"== msg3);
        System.out.println("third test done");
    }

    @Test
    public void sendToAll() throws IOException, InterruptedException {
//Setting up listeners to test who recieves the message
        System.out.println("STARTING FOURTH TEST: ");
        EchoListener listen1 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg=data;
            }
        };
        boyko.registerEchoListener(listen1);
        
        EchoListener listen2 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg2=data;
            }
        };
        nik.registerEchoListener(listen2);
        
        EchoListener listen3 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg3=data;
            }
        };
        peter.registerEchoListener(listen3);
        
        //Connecting the Superman
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);
        
        //Sending the message to Boyko and Nik
        System.out.println("Superman to all:");
        superman.send("SEND#*#Hey dudes");
        
        Thread.sleep(100);
        assertEquals("MESSAGE#Superman#Hey dudes", msg);
        assertEquals("MESSAGE#Superman#Hey dudes", msg2);
        assertEquals("MESSAGE#Superman#Hey dudes", msg3);
        System.out.println("fourth test done");
    }

    @Test
    public void sendNonsense() throws IOException, InterruptedException {
System.out.println("STARTING FIFTH TEST: ");
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };

        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(100);
        msg = "hej";
        superman.send("lolfag");
        Thread.sleep(100);

//        assertEquals("hej", msg);
        System.out.println("fifth test done");

    }

    //-----------------CLOSE-----------------
    @Test
    public void close() throws InterruptedException, IOException {
        System.out.println("STARTING SIXTH TEST: ");
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };

        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(100);
        msg = "lol";
        superman.send("CLOSE#");
        
        Thread.sleep(100);
        System.out.println("Trying sixth:");
        assertEquals("CLOSE#", msg);
        System.out.println("sixth test done");

    }

}