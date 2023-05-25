package visualize;

import db.Database;
import db.Table;
import visualize.data.AccountData;
import visualize.data.PersonaData;

public class DataCentral {
    public static String dataLocation = "svDb.txt";
    public static Database dataBaseAtlas = new Database();

    public static void registerTable(Table dataTable) {
        dataBaseAtlas.addTable(dataTable);
    }

    public static void saveChanges() {
        dataBaseAtlas.serialize(dataLocation);
    }

    static DataManager<AccountData> accountManager = new DataManager<>(new AccountData());
    static DataManager<PersonaData> personaManager = new DataManager<>(new PersonaData());
}
