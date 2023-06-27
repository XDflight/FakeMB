package server.structs;

import security.HashTool;
import server.DataManager;
import server.structs.annotations.*;
import util.ReflectHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static server.DataCentral.getDatasetOfClass;

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
        return this.getUUID().equals(UUID);
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
                if(field.isAnnotationPresent(RefMap.class)){
                    ArrayList<DataClass> refList=new ArrayList<>();

                    DataManager dataset= getDatasetOfClass(field.getAnnotation(RefMap.class).classType());
                    if(paramVal instanceof String){
                        String aString = (String) paramVal;
                        String[] refs=aString.split(",");
                        for (String s:
                                refs) {
                            refList.add(dataset.getByUUID(s));
                        }
                    }
                    fieldVal=refList;
                }
                if(field.isAnnotationPresent(Ref.class)){

                    DataManager dataset= getDatasetOfClass(field.getAnnotation(Ref.class).classType());
                    if(paramVal instanceof String){
                        String aString = (String) paramVal;
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

    private Object packVar(Field varType,Object var){

        if(varType.getType().getSimpleName().equals("Boolean")){
            return (Boolean) var ? "true" : "false";
        }
        if(varType.isAnnotationPresent(RefMap.class)){
            if(var instanceof Map<?,?>){
                StringBuilder build= new StringBuilder();
                Map<String,DataClass> refList= (Map<String, DataClass>) var;
                for (Map.Entry<String,DataClass> entry:
                     refList.entrySet()) {
                    build.append(entry.getKey()).append(",");
//                    build+=entry.getValue().getUUID()+",";
                }
                return build.toString();
            }else{
                System.out.println("A refMap variable can not be a "+var.getClass().getSimpleName()+" instance");
            }
        }
        if(varType.isAnnotationPresent(Ref.class)){

            if(var instanceof DataClass){
                return ((DataClass) var).getUUID();
            }else{
                System.out.println("A ref variable can not be a "+var.getClass().getSimpleName()+" instance");
            }

        }
        //Fallback option: String
        return var.toString();
    }

    private Object unPackVar_(Field varType,Object val){

        System.out.println("row-column's class is "+val.getClass().getSimpleName());

        if(varType.isAnnotationPresent(RefMap.class)){

            if(varType.getType().getSimpleName().equals("Boolean")){
                return ((String) val).equalsIgnoreCase("true");
            }

            if(val instanceof String){
                Map<String,DataClass> refMap=new HashMap<>();
                DataManager dataset= getDatasetOfClass(varType.getAnnotation(RefMap.class).classType());

                String aString = (String) val;
                String[] refs=aString.split(",");
                for (String ref:
                        refs) {
                    refMap.put(ref,dataset.getByUUID(ref));
                }
                return refMap;
            }else{
                System.out.println("A refMap variable can not be read from a "+val.getClass().getSimpleName()+" instance");
            }
        }
        if(varType.isAnnotationPresent(Ref.class)){

            if(val instanceof String){
                DataManager dataset= getDatasetOfClass(varType.getAnnotation(Ref.class).classType());
                String aString = (String) val;
                return dataset.getByUUID(aString);
            }else{
                System.out.println("A ref variable can not be read from a "+val.getClass().getSimpleName()+" instance");
            }

        }
        return val;
    }

    public Map<String,Object> toRow(){
        Map<String,Object> row=new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            String fieldName=field.getName();

            if(row.containsKey(fieldName)){
                try {
                    Object fieldVar=field.get(this);
                    row.put(fieldName,packVar(field,fieldVar));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                row.put(fieldName,null);
            }
        }
        return row;
    }
    public Field getAnnotatedField(Class<? extends Annotation> annotationClass){
        for (Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(annotationClass)){
                return field;
            }
        }
        return null;
    }
    public void unionMap(Map<String,DataClass> in){
        Field refMapField=getAnnotatedField(RefMap.class);
        Map<String,DataClass> refMap= null;
        try {
            refMap = (Map<String, DataClass>) refMapField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        assert refMap != null;
        refMap.putAll(in);
    }
    public void removeBy(DataClass filter){
        Field refMapField=getAnnotatedField(RefMap.class);
        Map<String,DataClass> refMap= null;
        try {
            refMap = (Map<String, DataClass>) refMapField.get(this);

            Map<String, DataClass> newMap = new HashMap<>();
            for (Map.Entry<String,DataClass> et: refMap.entrySet()) {
                if(!et.getValue().filterBy(filter)) {
                    newMap.put(et.getKey(),et.getValue());
                }
            }

            refMapField.set(this,newMap);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public DataClass readRow(Map<String,Object> row){
        for (Field field : this.getClass().getDeclaredFields()) {
            String fieldName=field.getName();
            Object fieldVal=row.get(field.getName());

            if(row.containsKey(fieldName)){
                try {
                    field.set(this,unPackVar_(field,fieldVal));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    field.set(this,null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }
}