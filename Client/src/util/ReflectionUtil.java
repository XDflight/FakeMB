package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ReflectionUtil {
    public static ArrayList<Field> getCertainFields(Class<?> in, Predicate<Field> condition){

        ArrayList<Field> fields=new ArrayList<>();
        for(Field field:in.getDeclaredFields()){
            if (!Modifier.isStatic(field.getModifiers()) && condition.test(field)) {
                fields.add(field);
            }
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
