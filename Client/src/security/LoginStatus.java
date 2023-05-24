package security;

import systematic.Entity;

import java.util.UUID;

public class LoginStatus {
    static boolean hasAccount;
    static int permissionLevel;
    static long storageLocation;

    public static void loadFromSQL(UUID in){
        //do something
    }

    public static int getPermissionLevel() {
        return permissionLevel;
    }
    public static void setPermissionLevel(int level) {
        permissionLevel=level;
    }



    public static boolean hasPermissionLevel(int lvl) {
        return permissionLevel>=lvl;
    }
}
