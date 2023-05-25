package visualize;

import db.Database;
import db.Table;
import visualize.data.AccountData;

public class DataCentral {
    public static String dataLocation="/data/svDb.txt";
    public static Database dataBaseAtlas=new Database();
    public static void registerTable(Table dataTable){
        dataBaseAtlas.addTable(dataTable);
    }
    public static void saveChanges(){
        dataBaseAtlas.serialize(dataLocation);
    }

    static DataManager<AccountData> accountManager=new DataManager<>(new AccountData());
    static DataManager<AccountData> personaManager=new DataManager<>(new AccountData());
}
