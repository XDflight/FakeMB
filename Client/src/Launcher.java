import util.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
            e.printStackTrace();
        }
    }
}
