package httpsrvr;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPServer {

    static Integer port = 8081;
    static String ip = "localhost";

    public static void main(String[] args) throws IOException {
        if (args.length >= 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        InetSocketAddress i = new InetSocketAddress(ip, port);
        HttpServer server = HttpServer.create(i, 0);
        server.createContext("/", new RequestForIndex());
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

            String requestedFile = he.getRequestURI().toString();
            String[] l = requestedFile.split("/");
            requestedFile = l[l.length-1];
            System.out.println("URL "+requestedFile);
            //System.out.println("req" + requestedFile);
//            String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
            String mime = "";
            if (requestedFile.endsWith(".html")) {
                mime = "text/html";
            }
            else if (requestedFile.endsWith(".jar")){
                mime = "application/java-archive";
            }
            System.out.println("mime: " + mime);
            //else mine = "application/pdf || application/java-archive
            String contentFolder = "C:\\Users\\bobkoo7\\Documents\\NetBeansProjects\\ChatServerBoyko\\dist\\public\\";
//            File file = new File(contentFolder + f);
            File file = new File(contentFolder + requestedFile);

            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", mime);
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }

        }

    }

    static class RequestForLogFile implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String fileName = "dist\\chatLog.txt";

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            List<String> linesFromFile = new ArrayList();
            try {

                String line = br.readLine();

                while (line != null) {
                    linesFromFile.add(line);
                    line = br.readLine();
                }
            } finally {
                br.close();
            }

            String response = "";
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<font color=\"white\">");
            sb.append("<title>Log file</title>\n");

            sb.append("<body background= http://desktopbackgroundshq.com/backgrounds/cool-water-bottle-cool-backgrounds-bottle-water-26068.jpg>\n");
            sb.append("<table border=\"3\">\n");
            sb.append("<caption> This table represents our server log file </caption>\n");
            sb.append("<tr><th colspan=\"2\"> Log file</th></tr>\n");
            Map<String, String> structuredLinkedHashMapViaExistingList = new LinkedHashMap();
            String temp = "probe";
            boolean trigger = false;
            for (int i = 0; i < linesFromFile.size(); i++) {
                if (trigger) {
                    structuredLinkedHashMapViaExistingList.put(temp, linesFromFile.get(i));
                    trigger = false;
                } else {
                    temp = linesFromFile.get(i);
                    structuredLinkedHashMapViaExistingList.put(temp, "");
                    trigger = true;

                }
            }
            for (Map.Entry<String, String> entry : structuredLinkedHashMapViaExistingList.entrySet()) {
                sb.append("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>\n");
            }
            sb.append("</table>\n");
            sb.append("<li><code>Go back to main page: </code><a href=http://haladin.cloudapp.net:8080/index.html>click</a><br/>");
            sb.append("</font>");
            sb.append("</body>");

            sb.append("</html>");

            response = sb.toString();
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.getResponseHeaders();
            he.sendResponseHeaders(200, response.length());

            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response);
            }

        }

    }

    static class RequestForClientInformation implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String fileName = "dist\\clientInfo.txt";

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            List<String> linesFromFile = new ArrayList();
            try {

                String line = br.readLine();

                while (line != null) {
                    linesFromFile.add(line);
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
            String names = "";
            Integer size = linesFromFile.size();
            for (int i = 0; i < size; i++) {
                if (i < size - 1) {
                    names += linesFromFile.get(i) + ",";
                } else {
                    names += linesFromFile.get(i);
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<font color=\"white\">");
            sb.append("<title>Online Users</title>\n");

            sb.append("<style>\n");
            sb.append(".bobkoo span{\n");
            sb.append("font-family: Comic Sans MS;\n");
            sb.append("font-size: 30px;}\n");
            sb.append(".digidaidai span{\n");
            sb.append("font-family: verdana;\n");
            sb.append("font-size: 20px;}\n");
            sb.append("</style>\n");
            sb.append("<body background=\"http://desktopbackgroundshq.com/backgrounds/cool-water-bottle-cool-backgrounds-bottle-water-26068.jpg\">\n");
            sb.append("<div class =\"bobkoo\"> <span><code>Welcome to our counter:</code></span> </div>\n");
            sb.append("<div class =\"digidaidai\"> <span> Current number of users online : <i>" + (size + 1) + "</i></span></div>\n");
            sb.append("Names of people online: <b>" + names + "</b><br/><br/<br/>\n");
            sb.append("<div class =\"digidaidai\"> <span> T a k</span></div></br>\n");
            //sb.append("<a href=\"/home/bobkoo/Downloads/Semester 3/HTML and CSS/main\">&lt;&lt;&lt;Back</a>\n");
            sb.append("<li><code>Go back to main page: </code><a href=http://haladin.cloudapp.net:8080/index.html>click</a><br/>");
            sb.append("</font>");
            sb.append("</body>");

            sb.append("</html>");

            String content = sb.toString();
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.getResponseHeaders();
            he.sendResponseHeaders(200, content.length());

            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(content);
            }
        }

    }

}
