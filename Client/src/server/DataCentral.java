package server;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeTags;
import db.Database;
import db.Table;
import server.structs.DataClass;
import server.structs.annotations.ComplexData;
import util.ConfigUtil;
import util.FileUtils;
import util.MySqlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static commands.Commands.rootCommandNode;

public class DataCentral {
    public static String dataLocation = "./data/svDb.txt";
    public static Database dataBaseAtlas = new Database();

    public static void registerTable(Table dataTable) {
        dataBaseAtlas.addTable(dataTable);
    }

    public static void changeDbSystemTo(String newSystem){
        String oldSystem= ConfigUtil.getConfig("dbSystem");
        if(oldSystem.equals(newSystem)){
            System.out.println("Already in "+newSystem+" db system!");
            return;
        }
        switch (newSystem){
            case "mySql"->{
                MySqlUtil.writeData(FileUtils.readFile(dataLocation));
            }
            case "local"->{
                FileUtils.writeFile(dataLocation,MySqlUtil.readData());
            }
        }
        System.out.println("Pushed data from "+oldSystem+" to "+newSystem+" database");
        ConfigUtil.setConfig("dbSystem",newSystem);
    }
    public static void saveChanges() {
        for(Class<?> classType:dataManagers.keySet()){
            dataManagers.get(classType).loadToTable();
        }
        dataBaseAtlas.serialize(dataLocation);
    }
    public static void loadDB() {
        dataBaseAtlas.deserialize(dataLocation);
        ArrayList<DataManager>deferredLoad=new ArrayList<>();
        for(Class<?> classType:dataManagers.keySet()){
            if(classType.isAnnotationPresent(ComplexData.class)){
                deferredLoad.add(dataManagers.get(classType));
            }else{
                dataManagers.get(classType).loadFromTable();
            }
        }
        for (DataManager dataset:
             deferredLoad) {
            dataset.loadFromTable();
        }
    }

    public static Map<Class<?>, DataManager> dataManagers = new HashMap<>();

    public static void printDataset(){
        for (Map.Entry<Class<?>, DataManager> entry:
             dataManagers.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void registerDataType(Class<?> classIn) {
        dataManagers.put(classIn, new DataManager(classIn));
        rootCommandNode.then(CommandNode.makeAccountCommands(dataManagers.get(classIn), classIn, true));
        rootCommandNode.then(CommandNode.makeAccountCommands(dataManagers.get(classIn), classIn, false));

        CommandNode editNode = new CommandNodeFork("edit").then
                (new CommandNodeFork(classIn.getSimpleName()).then
                        (new CommandNodeTags("editTags").end(
                                (context -> {
                                        if(SearchGroup.filteredGroup==null){
                                            System.out.println("SearchGroupNullException");
                                        }else{
                                            DataManager manager=dataManagers.get(classIn);
                                            SearchGroup.filteredGroup.forEach((data)->{
                                                data.editBy(manager.rowToObject(context.parameters));
                                            });
                                        }
                                }),
                                0
                        ))
                );

        CommandNode removeNode = new CommandNodeFork("remove").then
                (new CommandNodeFork(classIn.getSimpleName()).then
                        (new CommandNodeTags("editTags").end(
                                (context -> {
                                    if(SearchGroup.filteredGroup==null){
                                        System.out.println("SearchGroupNullException");
                                    }else{
                                        DataManager manager=dataManagers.get(classIn);
                                        SearchGroup.filteredGroup.forEach((data)->{
                                            data.editBy(manager.rowToObject(context.parameters));
                                        });
                                    }
                                }),
                                0
                        ))
                );
        CommandNode filterNode = new CommandNodeFork("filter")
                .then(
                        new CommandNodeFork(classIn.getSimpleName())
                                .then(
                                        new CommandNodeTags("filterTags")
                                                .end(
                                                        (context -> {
                                                            DataManager manager=dataManagers.get(classIn);
                                                            SearchGroup.filteredGroup=manager.filterBy(manager.rowToObject(context.parameters));
                                                            System.out.println(SearchGroup.filteredGroup);
                                                        }),
                                                        0
                                                )
                                )

                );

        rootCommandNode.then(editNode).then(filterNode);
    }

    public static void registerDataType(DataClass classObjectIn) {
        Class<?> classIn = classObjectIn.getClass();
        registerDataType(classIn);
    }

    public static DataManager getDatasetOfClass(Class<?> classIn){
        return dataManagers.get(classIn);
    }

    public static void addEntry(DataClass data) {
        dataManagers.get(data.getClass()).addEntry(data);
    }

    public static boolean hasEntry(DataClass data) {
        return dataManagers.get(data.getClass()).hasEntry(data);
    }
}
