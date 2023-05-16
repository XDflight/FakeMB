package CommandCore;

import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<String,Object> parameters=new HashMap<>();
    public Context(){

    }
    public void add(String name,String type,String value){
        Object converted;
        switch (type){
            case "String":
                converted=value;
                break;
            case "Double":
                converted=Double.parseDouble(value);
                break;
            case "Integer":
                converted=Integer.parseInt(value);
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
