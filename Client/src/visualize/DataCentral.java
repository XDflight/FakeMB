package visualize;

import db.Database;
import db.Table;
import visualize.awake.AccountData;

public class DataCentral {
    public static String dataLocation="/data/svDb";
    public static Database dataBaseAtlas=new Database();
    public static void registerTable(Table dataTable){
        dataBaseAtlas.addTable(dataTable);
    }
    public static void saveChanges(){
        dataBaseAtlas.serialize(dataLocation);
    }
    public static void initialize(){
        DataManager<AccountData> AccountManager=new DataManager<>(new AccountData());
    }
}
