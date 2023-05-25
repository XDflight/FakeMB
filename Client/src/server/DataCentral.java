package server;

import commandCore.Command;
import db.Database;
import db.Table;
import server.structs.AccountData;
import server.structs.dataClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static commands.Commands.rootCommand;

public class DataCentral {
    public static String dataLocation="/data/svDb.txt";
    public static Database dataBaseAtlas=new Database();
    public static void registerTable(Table dataTable){
        dataBaseAtlas.addTable(dataTable);
    }
    public static void saveChanges(){
        dataBaseAtlas.serialize(dataLocation);
    }
    public static Map<Class<?>,DataManager> dataManagers=new HashMap<>();
    public static void registerDataType(Class<?> classIn){
        dataManagers.put(classIn,new DataManager(classIn));
        rootCommand.then(Command.dataOperation(classIn,true));
        rootCommand.then(Command.dataOperation(classIn,false));
    }
    public static void registerDataType(dataClass classObjectIn){
        Class<?> classIn=classObjectIn.getClass();
        dataManagers.put(classIn,new DataManager(classObjectIn));
        rootCommand.then(Command.dataOperation(classIn,true));
        rootCommand.then(Command.dataOperation(classIn,false));
    }
    public static void pushData(dataClass data){
        dataManagers.get(data.getClass()).addEntry(data);
    }
    public static boolean hasData(dataClass data){
        return dataManagers.get(data.getClass()).hasEntry(data);
    }
}
