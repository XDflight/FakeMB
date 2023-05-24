package visualize;

import db.Database;
import db.Table;

import javax.xml.crypto.Data;

public class DataCentral {
    public static String dataLocation="/data/svDb";
    public static Database dataBaseAtlas=new Database();
    public static void registerTable(Table dataTable){
        dataBaseAtlas.addTable(dataTable);
    }
    public static void saveChanges(){
        dataBaseAtlas.serialize(dataLocation);
    }
    public static void registerTables(){
        registerTable(AccountManager.tableSynced);
        registerTable(PersonaManager.tableSynced);
    }
}
