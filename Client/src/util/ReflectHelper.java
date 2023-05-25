package util;

import java.lang.reflect.Field;
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
}
