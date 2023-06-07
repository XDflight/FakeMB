package commandNodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Context {
    public Map<String,Object> parameters=new HashMap<>();
    public Context(){

    }
    public void add(String name,String type,Object value){
        Object converted = null;
        switch (type){
            case "String":
                converted=value;
                break;
            case "Double":
                converted=Double.parseDouble(value.toString());
                break;
            case "Integer":
                converted=Integer.parseInt(value.toString());
                break;
            case "ArrayList":
                if(value instanceof ArrayList<?>){
                    converted=(ArrayList<?>)value;
                }else{
                    System.out.println("IncompatibleTypeError");
                }
                break;
            default:
                converted=value;
                break;
        }
        parameters.put(name,converted);
    }
    public Object get(String name){
        return parameters.get(name);
    }
}
