package security;

import server.structs.AccountData;
import systematic.Entity;

import java.util.UUID;

public class LoginStatus {
    public static boolean hasAccount;
    public static int permissionLevel;

    public static boolean hasAccount() {
        return hasAccount;
    }
    public static void setHasAccount(boolean hasAccount) {
        LoginStatus.hasAccount = hasAccount;
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
