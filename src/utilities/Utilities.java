package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilities {

    public static Properties initProperties(String propertyFile) {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(propertyFile)) {
            properties.load(is);
        } catch (IOException ex) {
            System.out.println(String.format("Could not locate the %1$s file.", propertyFile));
            return null;
        }
        return properties;
    }

}
