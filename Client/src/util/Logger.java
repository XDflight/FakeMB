package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {
    private static void log2file(String message) {
        PrintWriter out = null;
        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
        try {
            out = new PrintWriter(new FileWriter("log"+(dateFormat.format(new Date()))+".txt", true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.write(message+"\n");
        out.close();

    }

    /** A simple helper method that prints to console and keeps a record at same time **/
    private void message(String in){
        System.out.println(in);
        log2file(in);
    }
    public void log(String in){
        message("[LOG] "+in);
    }
    public void warn(String in){
        message("[WARN] "+in);
    }
    public void error(String in){
        message("[ERROR] "+in);
    }
}
