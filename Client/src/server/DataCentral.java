package server;

import commandNodes.CommandNode;
import db.Database;
import db.Table;
import server.structs.dataClass;

import java.util.HashMap;
import java.util.Map;

import static commands.Commands.rootCommandNode;

public class DataCentral {
    public static String dataLocation = "./data/svDb.txt";
    public static Database dataBaseAtlas = new Database();

    public static void registerTable(Table dataTable) {
        dataBaseAtlas.addTable(dataTable);
    }

    public static void saveChanges() {
        dataBaseAtlas.serialize(dataLocation);
    }

    public static Map<Class<?>, DataManager> dataManagers = new HashMap<>();

    public static void registerDataType(Class<?> classIn) {
        dataManagers.put(classIn, new DataManager(classIn));
        rootCommandNode.then(CommandNode.dataOperation(dataManagers.get(classIn), classIn, true));
        rootCommandNode.then(CommandNode.dataOperation(dataManagers.get(classIn), classIn, false));
    }

    public static void registerDataType(dataClass classObjectIn) {
        Class<?> classIn = classObjectIn.getClass();
        registerDataType(classIn);
    }

    public static void pushData(dataClass data) {
        dataManagers.get(data.getClass()).addEntry(data);
    }

    public static boolean hasData(dataClass data) {
        return dataManagers.get(data.getClass()).hasEntry(data);
    }
}
