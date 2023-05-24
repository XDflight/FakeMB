package visualize;

import db.Table;
import security.HashTool;
import security.LoginStatus;

public class AccountManager extends DataManager {
    static{
        tableSynced=new Table("accounts")
            .addField("userName", String.class)
            .addField("hashPass", String.class)
            .addField("school_id", String.class);
    }

    public static void register(String userName,String password) {
        String hashed= HashTool.generate(password);
        tableSynced.addRow(null);
        int bottomIndex=tableSynced.getRowNum()-1;
        tableSynced.setValue(bottomIndex, "userName",userName);
        tableSynced.setValue(bottomIndex,"hashPass",hashed);
        //Todo: implement Binary search optimization
    }
    public static void removeAccount(String userName){
        for (int i = 0; i < tableSynced.getRowNum(); i++) {
            if(tableSynced.getValue(i,"userName").equals(userName)){
                tableSynced.removeRow(i);
            }
        }
    }
    public static void login(String userName,String password) {
        String hashed=HashTool.generate(password);
        boolean hasUser = false;
        for (int i = 0; i < tableSynced.getRowNum(); i++) {
           if(tableSynced.getValue(i,"userName").equals(userName)){
               hasUser=true;
               if(tableSynced.getValue(i,"hashPass").equals(hashed)){
                   System.out.println("Login Success");
//                   LoginStatus.setPermissionLevel();
               }
           }
        }
        if(!hasUser){
            System.out.println("UserName incorrect");
        }
        System.out.println("Login Faied");
    }
}