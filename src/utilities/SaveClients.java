
package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveClients
{
        public void savingClients(String tobeIncluded) {
        try {

            String content = tobeIncluded;

            //File file = new File("C:\\Users\\Haladin\\Desktop\\HaladinServer\\clientInfo.txt");
            File file = new File("C:\\Users\\Haladin\\Desktop\\HaladinServer2\\dist\\clientInfo.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
