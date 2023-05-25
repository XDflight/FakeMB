package visualize;

import db.Table;
import visualize.data.dataClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static visualize.DataCentral.registerTable;

public class DataManager<T extends dataClass> {
    dataClass templateDataClass;
    final Class dataClass;

    Table tableSynced;

    public DataManager(dataClass template){

        templateDataClass = template;
        dataClass = template.getClass();
        tableSynced=new Table(templateDataClass.getClass().toString());
        registerTable(tableSynced);
        for (Field field : dataClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                System.out.println("Found non-static field: " + field.getName());
                tableSynced.addField(field.getName(),field.getClass());
            }
        }
    }

    public Map<String,Object> objectToRow(dataClass in){
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

    public dataClass RowToObject(Map<String,Object> in){
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

        return (T) templateDataClass.makeClone();
    }

    public void setEntry(int rowIndex,dataClass data){
        tableSynced.setRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(int rowIndex,dataClass data){
        tableSynced.addRowRaw(rowIndex,objectToRow(data));
    }

    public void addEntry(dataClass data){
        tableSynced.addRowRaw(objectToRow(data));
    }

    public T getEntry(int rowIndex){
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

        return (T) templateDataClass.makeClone();
    }
}
