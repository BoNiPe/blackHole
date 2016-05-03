///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import client.Client;
//import client.EchoListener;
//import java.io.IOException;
//import org.junit.AfterClass;
//import static org.junit.Assert.*;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import server.Server;
//
///**
// *
// * @author Desting
// */
//public class TestClient {
//    
//    public TestClient() {
//    }
//    int no = 1;
//    String user;    
//    String msg;
//    
//    @BeforeClass
//    public static void setUpClass() throws IOException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Server.main(null);
//            }
//        }).start();
//        
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//        Server.stopServer();
//    }
//    
//    
//    @Test
//    public void connect() throws IOException, InterruptedException {
//        
//        
//        Client client1 = new Client();
//        EchoListener listen = new EchoListener()
//        {
//
//            @Override
//            public void messageArrived(String data) {
//                System.out.println(data);
//                msg = data;
//            }
//        };
//        client1.registerEchoListener(listen);
//        client1.connect("localhost", 9090);
//        client1.send("CONNECT#Boyko");
//        Thread.sleep(10);
//        
//        
//        
//
//        assertEquals("ONLINE#Boyko", msg);
//    }
//    
//    
//}
