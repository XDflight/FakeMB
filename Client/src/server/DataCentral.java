package server;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeTags;
import db.Database;
import db.Table;
import security.OperatorLevel;
import server.structs.DataClass;
import server.structs.annotations.ComplexData;
import util.ConfigUtil;
import util.FileIOUtil;
import util.MySqlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static commands.Commands.rootCommandNode;
import static util.StringUtil.clearSuffix;

public class DataCentral {
    public static String dataLocation = "./data/svDb.txt";
    public static Database dataBaseAtlas = new Database();

    public static Map<Class<?>, DataManager> dataManagers = new HashMap<>();

    public static void registerTable(Table dataTable) {
        dataBaseAtlas.addTable(dataTable);
    }

    public static void portStorageTo(String newSystem){
        String oldSystem= ConfigUtil.getConfig("dbSystem");
        if(oldSystem.equals(newSystem)){
            System.out.println("Already in "+newSystem+" db system!");
            return;
        }
        switch (newSystem){
            case "mySql":
                MySqlUtil.writeData(FileIOUtil.readFile(dataLocation));
                break;
            case "local":
                FileIOUtil.writeFile(dataLocation,MySqlUtil.readData());
                break;
        }
        System.out.println("Pushed data from "+oldSystem+" to "+newSystem+" database");
        ConfigUtil.setConfig("dbSystem",newSystem);
    }

    //S & L
    public static void saveToStorage() {
        for(Class<?> classType:dataManagers.keySet()){
            dataManagers.get(classType).loadToTable();
        }
        dataBaseAtlas.serialize(dataLocation);
    }
    public static void loadFromStorage() {
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


    public static void printDataset(){
        for (Map.Entry<Class<?>, DataManager> entry:
             dataManagers.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void registerDataType(Class<? extends DataClass> classIn) {
        registerDataType(classIn,false);
    }
    public static void registerDataType(Class<? extends DataClass> classIn,boolean asPortal) {
        dataManagers.put(classIn, new DataManager(classIn));

        if(asPortal) {
            rootCommandNode.then(CommandNode.makeAccountCommands(dataManagers.get(classIn), classIn, true));
            rootCommandNode.then(CommandNode.makeAccountCommands(dataManagers.get(classIn), classIn, false));
        }

        String dataSimpleName=clearSuffix(classIn.getSimpleName(),"Data");

        CommandNode editNode = new CommandNodeFork("edit").then
                (new CommandNodeFork(dataSimpleName).then
                        (new CommandNodeTags("editTags").end(
                                (context -> {
                                        if(SearchGroup.filteredGroup==null){
                                            System.out.println("SearchGroupNullException");
                                        }else{
                                            DataManager manager=dataManagers.get(classIn);

                                            for (DataClass data:
                                                    SearchGroup.filteredGroup.values()) {
                                                data.editBy(manager.rowToObject(context.parameters));

                                            }
                                            System.out.println(SearchGroup.filteredGroup);
                                        }
                                }),
                                OperatorLevel.TEACHER
                        ))
                );

        CommandNode removeNode = new CommandNodeFork("remove").then
                (new CommandNodeFork(dataSimpleName).then
                        (new CommandNodeTags("editTags").end(
                                (context -> {
                                    if(SearchGroup.filteredGroup==null || SearchGroup.filteredGroup.size()<=0){

                                        DataManager manager=dataManagers.get(classIn);
                                        manager.removeAll(manager.rowToObject(context.parameters));
                                        System.out.println("Removed in global db");
                                    }else{

                                        DataManager manager=dataManagers.get(classIn);
                                        for (DataClass data:
                                                SearchGroup.filteredGroup.values()) {
                                            data.removeBy(manager.rowToObject(context.parameters));
                                        }
                                        System.out.println("Dropped from "+SearchGroup.filteredGroup.values().toArray()[0].getClass().getSimpleName()+" map");
                                    }
                                }),
                                OperatorLevel.TEACHER
                        ))
                );


        CommandNode addNode = new CommandNodeFork("add").then
                (new CommandNodeFork(dataSimpleName).then
                        (new CommandNodeTags("editTags").end(
                                (context -> {
                                    if(SearchGroup.filteredGroup==null || SearchGroup.filteredGroup.size()==0){


                                        DataManager manager=dataManagers.get(classIn);
                                        manager.addEntry(manager.rowToObject(context.parameters));
                                        System.out.println("Added in global db");
                                    }else{

                                        DataManager manager=dataManagers.get(classIn);
                                        for (DataClass data:
                                        SearchGroup.filteredGroup.values()) {
                                            data.unionMap(manager.getAll(manager.rowToObject(context.parameters)));
                                        }
                                        System.out.println("Added to "+SearchGroup.filteredGroup.values().toArray()[0].getClass().getSimpleName()+" map");
                                    }
                                }),
                                OperatorLevel.TEACHER
                        ))
                );

        CommandNode filterNode = new CommandNodeFork("filter")
                .then(
                        new CommandNodeFork(dataSimpleName)
                                .then(
                                        new CommandNodeTags("filterTags")
                                                .end(
                                                        (context -> {
                                                            DataManager manager=dataManagers.get(classIn);
                                                            SearchGroup.filteredGroup=manager.getAll(manager.rowToObject(context.parameters));
                                                            System.out.println(SearchGroup.filteredGroup);
                                                        }),
                                                        OperatorLevel.STUDENT
                                                )
                                )

                );

        CommandNode unfilterNode = new CommandNodeFork("unfilter")
                .end(
                        (context->{
                            SearchGroup.filteredGroup = null;
                        }),
                        OperatorLevel.STUDENT
                );

        rootCommandNode
                .then(editNode)
                .then(filterNode)
                .then(removeNode)
                .then(addNode)
                .then(unfilterNode);
    }

    public static DataManager getDatasetOfClass(Class<?> classIn){
        return dataManagers.get(classIn);
    }
}
