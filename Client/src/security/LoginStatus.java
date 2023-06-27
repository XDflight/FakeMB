package security;


import server.structs.AccountData;

import java.util.UUID;

import static security.OperatorLevel.permissionByRole;

public class LoginStatus {
    static AccountData account;
    static boolean neglectPermission=true;

    public static boolean loggedIn() {
        return account!=null;

    }

    public static int getPermissionLevel() {
        if (account.persona == null) return 1;
        return permissionByRole(account.persona.userGroup);
    }

    public static String getUname() {
        return account.userName;
    }

    public static void setUser(AccountData accountIn) {
        account=accountIn;
    }
    public static AccountData getUser() {
        return account;
    }

    public static boolean hasPermissionLevel(int lvl) {
        if(neglectPermission){
            return true;
        }
        if(!loggedIn()){
            return lvl<=0;
        }
        if(account.isSuperAdmin != null && account.isSuperAdmin){
            return true;
        }
        return getPermissionLevel() >= lvl;
    }
}
