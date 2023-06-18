package util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
    public static String getConfig(String configName){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("Client/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(configName);
    }
    public static void setConfig(String configName,String value){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("Client/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties.setProperty(configName,value);
        try {
            properties.store(new FileWriter("Client/config.properties"),"this is an comment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
