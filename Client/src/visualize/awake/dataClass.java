package visualize.awake;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
}