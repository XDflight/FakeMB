package server;

import db.Table;
import server.structs.DataClass;
import server.structs.annotations.Ref;
import server.structs.annotations.RefMap;
import util.OTBridgeUtil;
import util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static server.DataCentral.*;

public class DataManager {
    DataClass templateDataClass;
    public final Class dataClass;

    Table tableSynced;

    public Map<Object,DataClass> objectMap=new HashMap<>();

    public void dumpTable(){
        tableSynced.forEach((row)->{
            System.out.println(row);
        });
    }

    public DataManager(Class<?> classIn) {
        templateDataClass = (DataClass) ReflectionUtil.classInstance(classIn);
        dataClass = classIn;
        tableSynced=new Table(classIn.getSimpleName());
        registerTable(tableSynced);
        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                //Fixed
                tableSynced.addField(field.getName(),field.getType());
            }
        }
    }

    public void loadToTable(){
        AtomicInteger i= new AtomicInteger();
        objectMap.forEach((k,v)->{
            if(i.get()<tableSynced.getRowNum()){
                tableSynced.setRowRaw(i.getAndIncrement(),objectToRow(v));
            }else{
                tableSynced.addRowRaw(i.getAndIncrement(),objectToRow(v));
            }
        });
        tableSynced.forEach((row)->{
            System.out.println(row);
        });
//        for (int i = 0; i < objectArray.size(); i++) {
//            tableSynced.setRowRaw(il,objectToRow(objectArray.get(i)));
//        }
    }

    public void loadFromTable(){
        System.out.println(dataClass.getSimpleName());
        objectMap=new HashMap<>();
        tableSynced.forEach((row)->{
            System.out.println("row ");
            System.out.println(row);
        });
        tableSynced.forEach((row)->{
            DataClass rowObject=rowToObject(row);
            objectMap.put(rowObject.getUUID(),rowObject);
//            objectArray.add(rowObject);
        });
    }

    public void addEntry(DataClass data){
        Object uuid=data.getUUID();
        if(objectMap.containsKey(uuid)){
            System.out.println("A "+templateDataClass.getClass().getSimpleName()+" entry is already registered.");
        }else{
            objectMap.put(data.getUUID(),data);
        }
    }

    public DataClass getEntry(Object UUID){
        return objectMap.get(UUID);
    }

    public DataClass getLoginEntry(DataClass dataEntry){
        DataClass targetEntry=objectMap.get(dataEntry.getUUID());
        if(targetEntry==null){
            System.out.println("Account is invalid");
            return null;
        }
        if(dataEntry.loginEqual(targetEntry)){
            return targetEntry;
        }else{
            System.out.println("Password is incorrect");
            return null;
        }
    }

    @Override
    public String toString() {
        String build= "";
        System.out.println(dataClass.getSimpleName()+" base\n");
        for (Map.Entry<Object, DataClass> entry : objectMap.entrySet()) {
            Object k = entry.getKey();
            DataClass value = entry.getValue();
            build += value.toString();
            build += "\n";
        }
        return build;
    }


    public Map<String, DataClass> getAll(DataClass filter){
        Map<String, DataClass> result=new HashMap<>();
        objectMap.forEach((k,v)->{
            if(v.filterBy(filter)) {
                result.put((String)v.getUUID(),v);
            }
        });
        return result;
    }

    public void removeAll(DataClass filter){

        Set<Map.Entry<Object, DataClass>> entries=objectMap.entrySet();


        Map<Object, DataClass> newMap = new HashMap<>();
        for (Map.Entry<Object, DataClass> entry:
                entries) {

            if(!entry.getValue().filterBy(filter)){
                System.out.println("Keep: ");
                System.out.println(entry.getValue());
                newMap.put((String) entry.getKey(),entry.getValue());
            }
        }

        objectMap = newMap;
    }

    public Map<String,Object> objectToRow(DataClass in){
        return OTBridgeUtil.entryToRow(dataClass,in);
    }

    public DataClass rowToObject(Map<String,Object> in){
        return OTBridgeUtil.rowToEntry(dataClass,in);
    }


}
