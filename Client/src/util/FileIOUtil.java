package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIOUtil {
    public static void writeFile(String filePath,String text){
        try {
            Path path = Paths.get(filePath);
            Path parent = path.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            FileOutputStream fop = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(fop, "GBK");
            writer.append(text);
            writer.close();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFile(String filePath){
        StringBuilder file= new StringBuilder();
        try {
            FileInputStream fip = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fip, "GBK");
            while (reader.ready()) {
                file.append((char) reader.read());
            }
            reader.close();
            fip.close();
        } catch (FileNotFoundException e) {
            file.append("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();
    }
}
