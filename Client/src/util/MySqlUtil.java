package util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUtil {
    private static final MySqlHandler instance=new MySqlHandler();
    public static void writeData(String data){
        Object[] objs = {};
        ResultSet set = instance.select("select * from wrapper where id=1", objs);
        int cnt=0;
        try {
            while (set.next()) {
                cnt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("queried rows: "+cnt+".");
        if(cnt==1){
            System.out.println("do modify");
            int res_modify = instance.update("update wrapper set value='"+data+"' where id=1", objs);
            System.out.println("Affected "+res_modify+" rows.");
        }
        if(cnt==0){
            System.out.println("do insert");
            int res_add = instance.update("insert into wrapper set value='"+data+"', id=1", objs);
            System.out.println("Affected "+res_add+" rows.");
        }
    }

    public static String readData(){
        Object[] objs = {};
        ResultSet set = instance.select("select * from wrapper where id=1", objs);
        int cnt=0;
        String val = "";
        try {
            while (set.next()) {
                cnt++;
                val = set.getString("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cnt==1){
            return val;
        }
        if(cnt==0){
            return "null";
        }
        return "multiple forks";
    }
}
