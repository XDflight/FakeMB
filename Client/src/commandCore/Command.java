package commandCore;

import security.LoginStatus;
import security.OperatorLevel;
import util.Logger;
import server.structs.dataClass;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static util.StringHelper.getTrueTypeString;

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
    public String toString(){
        return children.toString();
    }
    public static Command dataOperation(Class<?> dataType,boolean checkOrAdd){
        Command command = new CommandFork(
                "create"+getTrueTypeString(dataType.getTypeName())
        );
        return command.consumeVars(ReflectHelper.getFields(dataType),checkOrAdd);
    }
    public Command consumeVars(ArrayList<Field> abs,boolean checkOrAdd){
        if(abs.size()<=0){
            this.end(
                    context -> {
                        if(checkOrAdd){

                            System.out.println("checked object");
                        }else{
                            System.out.println("added object");
                        }
                    },
                    0
            );
        }else{
            Command command= new CommandInput(abs.get(0).getName(),abs.get(0).getClass().toString());
            abs.remove(0);
            this.then(command.consumeVars(abs,checkOrAdd));
        }
        return this;
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