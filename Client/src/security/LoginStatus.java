package security;


import java.util.UUID;

public class LoginStatus {
    private static boolean debug = true;

    static String uname;
    static int permissionLevel;

    public static boolean loggedIn() {
        return permissionLevel > 0;
    }

    public static int getPermissionLevel() {
        return permissionLevel;
    }

    public static void setPermissionLevel(int level) {
        permissionLevel = level;
    }

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname_) {
        uname = uname_;
    }

    public static boolean hasPermissionLevel(int lvl) {
        return debug || permissionLevel >= lvl;
    }
}
