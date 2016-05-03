// PETER TOMASCIK FINAL VERSION OF HTTP SERVER
package httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HTTPServer {

    static Integer port = 8080;
    static String ip = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        if (args.length >= 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        InetSocketAddress i = new InetSocketAddress(ip, port);
        HttpServer server = HttpServer.create(i, 0);
        server.createContext("/index", new RequestForIndex());
        server.createContext("/logFile", new RequestForLogFile());
        server.createContext("/clientInformation", new RequestForClientInformation());

        server.setExecutor(null);
        server.start();
        System.out.println("IP address : " + ip);
        System.out.println("Port number : " + port);
    }

    static class RequestForIndex implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String contentFolder = "/home/bobkoo/NetBeansProjects/ChatServerv1.0/ChatServerv1/src/httpServer/";
            File file = new File(contentFolder + "index.html");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }

        }

    }

    static class RequestForLogFile implements HttpHandler {

        // look at it   !

        @Override
        public void handle(HttpExchange he) throws IOException {

            String contentFolder = "/home/bobkoo/NetBeansProjects/ChatServerFromRabbit/blackHole-TestV3/";
            File file = new File(contentFolder + "chatterBox.txt");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }

        }

    }

    static class RequestForClientInformation implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String contentFolder = "/home/bobkoo/NetBeansProjects/ChatServerFromRabbit/blackHole-TestV3/";
            File file = new File(contentFolder + "clientInfo.txt");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }

    }

}
