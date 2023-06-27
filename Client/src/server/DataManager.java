package server;

import db.Table;
import server.structs.DataClass;
import server.structs.annotations.Ref;
import server.structs.annotations.RefMap;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static server.DataCentral.*;

public class DataManager {
    DataClass templateDataClass;
    final Class dataClass;

    Table tableSynced;

    Map<Object,DataClass> objectMap;
    ArrayList<DataClass> objectArray;

    @Override
    public String toString() {
        String build= "";
        System.out.println(templateDataClass.getClass().getSimpleName()+" base\n");
        for (Map.Entry<Object, DataClass> entry : objectMap.entrySet()) {
            Object k = entry.getKey();
            DataClass value = entry.getValue();
            build += value.toString();
            build += "\n";
        }
        return build;
    }

    public DataManager(DataClass template){

        templateDataClass = template;
        dataClass = template.getClass();
        tableSynced=new Table(templateDataClass.getClass().toString());
        registerTable(tableSynced);
        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                tableSynced.addField(field.getName(),field.getClass());
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
//        for (int i = 0; i < objectArray.size(); i++) {
//            tableSynced.setRowRaw(il,objectToRow(objectArray.get(i)));
//        }
    }
    
    public void loadFromTable(){
        objectMap=new HashMap<>();
        tableSynced.forEach((row)->{
            DataClass rowObject=rowToObject(row);
            objectMap.put(rowObject.getUUID(),rowObject);
//            objectArray.add(rowObject);
        });
    }
    public Map<String, DataClass> filterBy(DataClass filter){
        Map<String, DataClass> result=new HashMap<>();
        objectMap.forEach((k,v)->{
            if(v.filterBy(filter)) {
                result.put((String)v.getUUID(),v);
            }
        });
        return result;
    }
    public ArrayList<DataClass> removeBy(DataClass filter){
        ArrayList<DataClass> result=new ArrayList<>();

        Set<Map.Entry<Object, DataClass>> entries=objectMap.entrySet();
        for (Map.Entry<Object, DataClass> entry:
                entries) {
            if(entry.getValue().filterBy(filter)){
                objectMap.remove(entry.getKey());
            }
        }

        return result;
    }
    public DataClass getByUUID(Object UUID){
        return objectMap.get(UUID);
    }

    public DataManager(Class<?> classIn) {
        templateDataClass= (DataClass) ReflectHelper.classInstance(classIn);
        dataClass = classIn;
        tableSynced=new Table(templateDataClass.getClass().toString());
        registerTable(tableSynced);
        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
//                System.out.println("Add Field");
                //Fixed
                tableSynced.addField(field.getName(),field.getType());
            }
        }
    }

    public Map<String,Object> objectToRow(DataClass in){
        Map<String,Object> row=new HashMap<>();

        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName=field.getName();

                try {
                    Object val=field.get(in);

                    if(field.getType().getSimpleName().equals("Boolean")){
                        val = (Boolean) (val==null?false:val) ? "true" : "false";
                    }

                    if(val!=null){
                        if(field.isAnnotationPresent(RefMap.class)){
                            String build="";
                            Map<String,DataClass> refMap= (Map<String, DataClass>) field.get(in);
                            for (DataClass data :
                                    refMap.values()) {
                                build+=data.getUUID()+",";
                            }
                            val=build;
                        }
                        if(field.isAnnotationPresent(Ref.class)){
                            DataClass ref= (DataClass) field.get(in);
                            val=ref.getUUID();
                        }
                    }

                    row.put(fieldName,val!=null?val:"null");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return row;
    }

    public DataClass rowToObject(Map<String,Object> in){
        Map<String,Object> row=in;

        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {

                String fieldName=field.getName();
                Object paramVal=row.get(field.getName());

                if(row.containsKey(fieldName)){
                    Object fieldVal=row.get(field.getName());

                    if(field.isAnnotationPresent(RefMap.class)){
                        Map<String,DataClass> refMap=new HashMap<>();

                        DataManager dataset= getDatasetOfClass(field.getAnnotation(RefMap.class).classType());
                        if(paramVal instanceof String){
                            String aString = (String) paramVal;
                            String[] refs=aString.split(",");
                            for (String s:
                                    refs) {
                                refMap.put(s,dataset.getByUUID(s));
                            }
                        }
                        fieldVal=refMap;
                    }
                    if(field.isAnnotationPresent(Ref.class)){

                        DataManager dataset= getDatasetOfClass(field.getAnnotation(Ref.class).classType());
                        if(paramVal instanceof String){
                            String aString = (String) paramVal;
                            fieldVal=dataset.getByUUID(aString);
                        }

                    }

                    if(field.getType().getSimpleName().equals("Boolean")){
                        fieldVal= ((String) fieldVal).equalsIgnoreCase("true");
                    }

                    try {
                        field.set(templateDataClass,fieldVal);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        field.set(templateDataClass,null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return (DataClass) templateDataClass.makeClone();
    }

    public boolean hasEntry(DataClass dataEntry){
        boolean[] has = {false};
        objectMap.forEach(
                (k,v)->{
                    if(v==null){
                        return;
                    }
                    if(dataEntry.fullEqual(v)&&!has[0]){
                        has[0] =true;
                    }
                }
        );
        return has[0];
    }
    public DataClass queryLogin(DataClass dataEntry){
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

    public void setEntry(int rowIndex, DataClass data){
        tableSynced.setRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(int rowIndex, DataClass data){
        tableSynced.addRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(DataClass data){
        Object uuid=data.getUUID();
        if(objectMap.containsKey(uuid)){
            System.out.println("A "+templateDataClass.getClass().getSimpleName()+" entry is already registered.");
        }else{
            objectMap.put(data.getUUID(),data);
        }
    }

    public DataClass getEntry(int rowIndex){
        Map<String,Object> row=tableSynced.getRowRaw(rowIndex);

        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName=field.getName();
                if(row.containsKey(fieldName)){
                    try {
                        field.set(templateDataClass,row.get(fieldName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        field.set(templateDataClass,null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return (DataClass) templateDataClass.makeClone();
    }
}
