package server;

//王博洋
//作为鲁大海的配音役，配音很硬气。大海的情感，思想纠结把握很精准，与周冲的对手戏对峙很劲，很有力
//
//张昊宇
//我觉得周冲的选词最能体现出人物的特点，同时配音的情感到位，有周冲的那种理想主义者的气质

import commandNodes.CommandNode;
import db.Database;
import db.Table;
import server.structs.DataClass;

import java.util.HashMap;
import java.util.Map;

import static commands.Commands.rootCommandNode;

public class DataCentral {
    public static String dataLocation = "./data/svDb.txt";
    public static Database dataBaseAtlas = new Database();

    public static void registerTable(Table dataTable) {
        dataBaseAtlas.addTable(dataTable);
    }

    public static void printDataBase(){

    }
    public static void saveChanges() {
        dataBaseAtlas.serialize(dataLocation);
    }
    public static void loadDB() {
        dataBaseAtlas.deserialize(dataLocation);
    }

    public static Map<Class<?>, DataManager> dataManagers = new HashMap<>();

    public static void registerDataType(Class<?> classIn) {
        dataManagers.put(classIn, new DataManager(classIn));
        rootCommandNode.then(CommandNode.dataOperation(dataManagers.get(classIn), classIn, true));
        rootCommandNode.then(CommandNode.dataOperation(dataManagers.get(classIn), classIn, false));
    }

    public static void registerDataType(DataClass classObjectIn) {
        Class<?> classIn = classObjectIn.getClass();
        registerDataType(classIn);
    }

    public static void pushData(DataClass data) {
        dataManagers.get(data.getClass()).addEntry(data);
    }

    public static boolean hasData(DataClass data) {
        return dataManagers.get(data.getClass()).hasEntry(data);
    }
}
