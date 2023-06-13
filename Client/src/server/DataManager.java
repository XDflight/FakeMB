package server;

import db.Table;
import server.structs.DataClass;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static server.DataCentral.registerTable;

public class DataManager {
    DataClass templateDataClass;
    final Class dataClass;

    Table tableSynced;
    
    ArrayList<DataClass> objectArray;

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
        for (int i = 0; i < objectArray.size(); i++) {
            tableSynced.setRowRaw(i,objectToRow(objectArray.get(i)));
        }
    }
    
    public void loadFromTable(){
        objectArray=new ArrayList<>();
        tableSynced.forEach((row)->{
            DataClass rowObject=rowToObject(row);
            objectArray.add(rowObject);
        });
    }
    public ArrayList<DataClass> filterBy(DataClass filter){
        ArrayList<DataClass> result=new ArrayList<>();
        for (DataClass data:
             objectArray) {
            if(data.filterBy(filter)){
                result.add(data);
            }else{
//                System.out.println("MisMatch");
            }
        }
        return result;
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
                    row.put(fieldName,field.get(in));
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

    public boolean hasEntry(DataClass dataEntry){
        boolean[] has = {false};
        tableSynced.forEach(
                (row)->{
                    if(row==null){
                        return;
                    }
                    if(dataEntry.fullEqual(rowToObject(row))&&!has[0]){
                        has[0] =true;
                    }
                }
        );
        return has[0];
    }
    public boolean canLogin(DataClass dataEntry){
        boolean[] has = {false};
        tableSynced.forEach(
                (row)->{
                    if(row==null){
                        return;
                    }
                    if(dataEntry.loginEqual(rowToObject(row))&&!has[0]){
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
        tableSynced.addRowRaw(objectToRow(data));
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
