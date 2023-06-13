package server.structs;

import security.HashTool;
import server.DataManager;
import server.structs.annotations.*;
import util.ReflectHelper;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;

import static server.DataCentral.getDataManager;

public class DataClass {
    public DataClass makeClone(){
        Object clone=ReflectHelper.classInstance(this.getClass());
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    field.set(clone,field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return (DataClass) clone;
    }
    public boolean fullEqual(DataClass target){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object varChecked=field.get(this);
                    Object varTarget=field.get(target);
                    if(varTarget==null){
                        return false;
                    }
                    if(varChecked==null){
                        return false;
                    }
                    if(!varTarget.equals(varChecked)){
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public boolean loginEqual(DataClass target){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    if(field.isAnnotationPresent(LoginRequired.class)){
                        Object varChecked=field.get(this);
                        Object varTarget=field.get(target);
                        if(varTarget==null){
                            return false;
                        }
                        if(varChecked==null){
                            return false;
                        }
                        if(!varTarget.equals(varChecked)){
                            return false;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public boolean equalByUUID(Object UUID) throws Exception{
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if(field.isAnnotationPresent(UUID.class)){
                    if(field.get(this).equals(UUID)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public Object getUUID(){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if(field.isAnnotationPresent(UUID.class)){
                    try {
                        return field.get(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    public boolean filterBy(DataClass filter){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object varChecked=field.get(this);
                    Object varFilter=field.get(filter);
//                    System.out.println("VarChecked");
//                    System.out.println(varChecked);
//                    System.out.println("VarFilter");
//                    System.out.println(varFilter);
                    if(varFilter==null){
//                        System.out.println("Continued");
                        continue;
                    }
                    if(varChecked==null){
//                        System.out.println("EmptyError");
                        return false;
                    }
                    if(!varFilter.equals(varChecked)){
//                        System.out.println("MisMatchError");
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public boolean editBy(DataClass target){
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object varTarget=field.get(target);
                    if(varTarget==null){
                        continue;
                    }
                    field.set(this,varTarget);
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
            Object paramVal=row.get(field.getName());

            if (!Modifier.isStatic(field.getModifiers())) {
                Object fieldVal=row.get(field.getName());

                if(field.isAnnotationPresent(HashElement.class) && fieldVal instanceof String){
                    fieldVal= HashTool.generate((String) fieldVal,field.getDeclaredAnnotationsByType(HashElement.class)[0].hashType());
                }
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
                    field.set(dataEntry,fieldVal);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataEntry;
    }

    public String safeToString(Object in){
        return in==null? "null" : in.toString();
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
                            .append(safeToString(field.get(this)))
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