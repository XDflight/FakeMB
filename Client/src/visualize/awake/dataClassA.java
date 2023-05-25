package visualize.awake;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class dataClassA {
    public String school_id,name,userGroup;
    public static void main(String[] args) {
        dataClassA studentTmp=new dataClassA();
//        for(Field field : dataClassA.class.getDeclaredFields()){
//            if(!Modifier.isStatic(field.getModifiers())){
//
//            }
//        }
        for (Field field : dataClassA.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                System.out.println("Found non-static field: " + field.getName());
                try {
                    field.set(studentTmp,"114514");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(studentTmp.name);
        System.out.println(studentTmp.school_id);
        System.out.println(studentTmp.userGroup);
//        for (Field field : dataClassA.class.getDeclaredFields()) {
//            if (!Modifier.isStatic(field.getModifiers())) {
//                System.out.println("Found non-static field: " + field.getName());
//                try {
//                    System.out.println("studentTmp's "+field.getName()+" is "+field.get(studentTmp));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
