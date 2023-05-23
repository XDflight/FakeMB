import util.Logger;

public class Launcher {
    public static void main(String[] args) {
        try {
            Main.main(args);
        } catch (Exception e) {
            String msg = e.getMessage();
            Logger logger = new Logger();
            logger.error(msg);
            e.printStackTrace();
        }
    }
}
