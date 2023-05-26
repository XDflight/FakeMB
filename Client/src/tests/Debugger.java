package tests;
import tests.test1;
import util.ReflectHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Debugger {
    static boolean isProduction=true;
    public static boolean isDebug(){
        return !isProduction;
    }
    public static void runTest(int testID){
        try {
            String testClassName="tests.test"+testID;
            Object data =  ReflectHelper.classInstance(testClassName);
            Class<?> testClass=Class.forName(testClassName);
            for(Method method:testClass.getMethods()){
                if(method.getName().equals("main")){
                    method.invoke(null,new Object[] {
                            new String[]{}
                    });
                    return;
                }
                System.out.println(method.getName());
            }
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
