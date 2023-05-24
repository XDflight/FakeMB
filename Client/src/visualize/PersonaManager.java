package visualize;

import db.Table;

import java.util.Map;

public class PersonaManager extends DataManager{
    static {
        tableSynced=new Table("accounts")
            .addField("school_id", String.class)
            .addField("name", String.class)
            .addField("userGroup", String.class)
            .addField("description", String.class)
            .addField("GPA", Integer.class);
    }
    public static void addPersona(String name,String userGroup){
        tableSynced.addRow(null);
        int bottomIndex=tableSynced.getRowNum()-1;
        tableSynced.setValue(bottomIndex, "name",name);
        tableSynced.setValue(bottomIndex,"userGroup",userGroup);
    }
    public static void removePersona(String by,String val){
        for (int i = 0; i < tableSynced.getRowNum(); i++) {
            if(tableSynced.getValue(i,by).equals(val)){
                tableSynced.removeRow(i);
            }
        }
    }
    public static Map<String, Object> getPersona(String by, String val){
        return tableSynced.getEntry((entry)->{
            return entry.get(by).equals(val);
        });
    }

}
