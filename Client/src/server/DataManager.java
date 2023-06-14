package server;

import db.Table;
import server.structs.DataClass;
import server.structs.annotations.Ref;
import server.structs.annotations.RefList;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    public ArrayList<DataClass> filterBy(DataClass filter){
        ArrayList<DataClass> result=new ArrayList<>();
        objectMap.forEach((k,v)->{
            if(v.filterBy(filter)) {
                result.add(v);
            }
        });
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
                    if(val!=null){
                        if(field.isAnnotationPresent(RefList.class)){
                            String build="";
                            ArrayList<DataClass> refList= (ArrayList<DataClass>) field.get(in);
                            for (DataClass data :
                                    refList) {
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

                    if(field.isAnnotationPresent(RefList.class)){
                        ArrayList<DataClass> refList=new ArrayList<>();

                        DataManager dataset=getDataManager(field.getAnnotation(RefList.class).classType());
                        if(paramVal instanceof String aString){
                            String[] refs=aString.split(",");
                            for (String s:
                                    refs) {
                                refList.add(dataset.getByUUID(s));
                            }
                        }
                        fieldVal=refList;
                    }
                    if(field.isAnnotationPresent(Ref.class)){

                        DataManager dataset=getDataManager(field.getAnnotation(Ref.class).classType());
                        if(paramVal instanceof String aString){
                            fieldVal=dataset.getByUUID(aString);
                        }

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
    public boolean canLogin(DataClass dataEntry){
        boolean[] has = {false};
        objectMap.forEach(
                (k,v)->{
                    if(v==null){
                        return;
                    }
                    if(dataEntry.loginEqual(v)&&!has[0]){
                        has[0] =true;
                    }
                }
        );
        return has[0];
    }

    public void setEntry(int rowIndex, DataClass data){
        tableSynced.setRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(int rowIndex, DataClass data){
        tableSynced.addRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(DataClass data){
        objectMap.put(data.getUUID(),data);
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
