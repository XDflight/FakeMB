package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ReflectHelper {
    public static ArrayList<Field> getFields(Object in){
        return getFields(in.getClass());
    }
    public static ArrayList<Field> getFields(Class<?> in){
        ArrayList<Field> fields=new ArrayList<>();
        for(Field field:in.getDeclaredFields()){
            fields.add(field);
        }
        return fields;
    }

    /**
     * @return an instance of given class
     */
    public static Object classInstance(Class<?> classIn){
        Constructor<?> ctor = null;
        Object instance = null;
        try {
            ctor = classIn.getConstructor();
            instance = ctor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * @param className full className, e.g. "mypackageGrand.mypackageFather.MyClass"
     * @return an instance of given class
     */
    public static Object classInstance(String className){
        Constructor<?> ctor;
        Object instance = null;
        Class<?> classIn = null;
        try {
            classIn = Class.forName(className);
            ctor = classIn.getConstructor();
            instance = ctor.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
