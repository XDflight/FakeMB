package security;

import java.util.HashMap;
import java.util.Map;

public class OperatorLevel {
    public static final int USER = 1;
    public static final int STUDENT = 2;
    public static final int TEACHER = 3;
    public static final int ADMIN = 4;
    public static Map<String,Integer> permissionByRole=new HashMap<>();
    static{
        permissionByRole.put("user",1);
        permissionByRole.put("student",2);
        permissionByRole.put("teacher",3);
        permissionByRole.put("admin",4);
    }
    public static int permissionByRole(String role){
        return permissionByRole.getOrDefault(role,1);
    }
}
