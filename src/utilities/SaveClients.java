/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author bobkoo
 */
public class SaveClients {

    public void savingClients(String tobeIncluded) {
        System.out.println(" I GO INSIDE YOUR ANUS!");
        try {

            String content = tobeIncluded;

            File file = new File("/home/bobkoo/NetBeansProjects/ChatServerFromRabbit/blackHole-TestV3/clientInfo.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
