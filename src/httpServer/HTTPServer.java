package httpServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilities.Utilities;

public class HTTPServer {

    private static final Properties properties = Utilities.initProperties("server.properties");

    public static void main(String[] args) throws IOException {
        new HTTPServer().reading();
    }

    public void reading() throws IOException {
//        String logFile = properties.getProperty("logFile");
//        Logger logger = Utilities.getLogger(logFile, HTTPServer.class.getName());
//        System.out.println("1. "+logger.toString());
//        for (Handler h : Logger.getLogger(HTTPServer.class.getName()).getHandlers())
//        {
//            System.out.println(h.toString());
//        }

        String fileName = "chatterBox.txt";


        BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        System.out.println(sb.toString());
    } finally {
        br.close();
    }


    }
}
