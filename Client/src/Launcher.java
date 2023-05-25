import util.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        try {
            Main.main(args);
        } catch (Exception e) {
            Writer buffer = new StringWriter();
            PrintWriter pw = new PrintWriter(buffer);
            e.printStackTrace(pw);
            String msg = buffer.toString();
            Logger logger = new Logger();
            logger.error(msg);
            System.out.println("Fatal error occurred.");
            System.out.println("Error log is saved.");
            System.out.println("Press \"enter\" to exit.");
            new Scanner(System.in).nextLine();
            System.exit(-1);
        }
    }
}
