package CommandCore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Command {
    public String name;
    public Consumer<Context> executable;
    Map<String,Command> children=new HashMap<>();
    boolean hasFork=false;
    boolean isParameterStarter=false;
    String nextParameter;



    public boolean hasFork() {
        return hasFork;
    }
    public boolean isEnd() {
        return !hasFork;
    }

    public Command then(Command in) {
        if(in instanceof CommandInput){
            if(isParameterStarter){
                try {
                    throw new Exception("Multiple Parameter Fork, unable to handle");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                nextParameter=in.name;
                isParameterStarter=true;
            }
        }
        children.put(in.name,in);
        hasFork=true;
        return this;
    }
    public Command end(Consumer<Context> in){
        executable=in;
        return this;
    }
    public Command progress(String in){
        if(isParameterStarter){
            return progress();
        }else{
            return children.containsKey(in)? children.get(in):null;
        }
    }
    public Command progress(){
        return children.get(nextParameter);
    }
}