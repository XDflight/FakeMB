package commandCore;

import security.LoginStatus;
import security.OperatorLevel;
import sun.rmi.runtime.Log;
import util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Command {
    public String name;
    private Consumer<Context> executable;
    private Predicate<LoginStatus> hasPower;

    Map<String,Command> children=new HashMap<>();
    boolean hasFork=false;
    boolean isParameterStarter=false;
    String nextParameter;

    Logger LOGGER=new Logger();

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
        OperatorLevel operatorLevel = OperatorLevel.ADMIN;
        Predicate<LoginStatus> predicate=(status)-> {
            return LoginStatus.hasPermissionLevel(3);
        };
        hasPower=predicate;
        return this;
    }
    public Command end(Consumer<Context> in,int level){
        executable=in;
        OperatorLevel operatorLevel = OperatorLevel.ADMIN;
        Predicate<LoginStatus> predicate=(status)-> {
            return LoginStatus.hasPermissionLevel(level);
        };
        hasPower=predicate;
        return this;
    }
    public Command end(Consumer<Context> in, Predicate<LoginStatus> predicate){
        executable=in;
        hasPower=predicate;
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
    public void run(Context params){
        if(hasPower.test(new LoginStatus())){
            executable.accept(params);
        }else{
            LOGGER.warn("no permission");
        }
    }
}