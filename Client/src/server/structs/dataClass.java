package server.structs;

import security.HashTool;
import server.structs.annotations.HashElement;
import util.Base64Helper;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class dataClass {
    public dataClass makeClone(){
        dataClass clone=new dataClass();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName=field.getName();
                System.out.println("Found non-static field: " + fieldName);
                try {
                    field.set(clone,field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return clone;
    }
    public boolean fullEqual(dataClass somebodyElse){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    if(
                            !field.get(this).equals(field.get(somebodyElse))
                    ){
                        return false;
                    };
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public static Object fromRow(Class<?> classIn,Map<String,Object> row){
        Object dataEntry = ReflectHelper.classInstance(classIn);
        for (Field field : classIn.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    field.set(dataEntry,row.get(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataEntry;
    }
    public static Object fromParam(Class<?> classIn,Map<String,Object> row){
        Object dataEntry = ReflectHelper.classInstance(classIn);
        for (Field field : classIn.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Object fieldVal=row.get(field.getName());
                if(field.isAnnotationPresent(HashElement.class) && fieldVal instanceof String){
                    fieldVal= HashTool.generate((String) fieldVal,field.getDeclaredAnnotationsByType(HashElement.class)[0].hashType());
                }
                try {
                    field.set(dataEntry,fieldVal);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataEntry;
    }
    public String toString(){
        StringBuilder append = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    append
                            .append("{")
                            .append(field.getName())
                            .append(":")
                            .append(field.get(this).toString())
                            .append("}")
                            .append("\n");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return append.toString();
    }
}