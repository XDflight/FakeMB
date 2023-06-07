package commandNodes;

import security.LoginStatus;
import security.OperatorLevel;
import server.DataManager;
import util.Logger;
import server.structs.DataClass;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static server.structs.DataClass.fromParam;
import static util.StringHelper.getTrueTypeString;

public class CommandNode {
    public String name;
    private Consumer<Context> executable;
    private Predicate<LoginStatus> hasPower;

    Map<String, CommandNode> children=new HashMap<>();
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
    public String toStringTop(){
        return name+"\n"+children.toString();
    }
    public String toString(){
        return children.toString();
    }
    public static CommandNode dataOperation(DataManager dataManager, Class<?> dataType, boolean checkOrAdd){
        String prefix=checkOrAdd?"login":"register";
        CommandNode commandNode = new CommandNodeFork(
                prefix+getTrueTypeString(dataType.getTypeName())
        );
        return commandNode.consumeVars(dataManager, dataType,ReflectHelper.getFields(dataType),checkOrAdd);
    }
    public CommandNode consumeVars(DataManager dataManager, Class<?> dataType, ArrayList<Field> abs, boolean checkOrAdd){
        if(abs.size()<=0){
            this.end(
                    context -> {
                        Object entry=fromParam(dataType,context.parameters);
                        System.out.println(entry);
                        if(checkOrAdd){
                            System.out.println( dataManager.hasEntry((DataClass) entry));
                            System.out.println("checked object");
                        }else{
                            dataManager.addEntry((DataClass) entry);
                            System.out.println("added object");
                        }
                    },
                    0
            );
        }else{
            CommandNode commandNode = new CommandNodeInput(abs.get(0).getName(),abs.get(0).getClass().toString());
            abs.remove(0);
            this.then(commandNode.consumeVars(dataManager,dataType,abs,checkOrAdd));
        }
        return this;
    }
    public CommandNode then(CommandNode in) {
        if(in instanceof CommandNodeInput || in instanceof CommandNodeTags){
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
        if(children.containsKey(in.name)){
            for (String key:
                 in.children.keySet()) {
                children.get(in.name).then(in.children.get(key));
            }
        }else{
            children.put(in.name,in);
        }
        hasFork=true;
        return this;
    }
    public CommandNode end(Consumer<Context> in){
        executable=in;
        OperatorLevel operatorLevel = OperatorLevel.ADMIN;
        Predicate<LoginStatus> predicate=(status)-> {
            return LoginStatus.hasPermissionLevel(3);
        };
        hasPower=predicate;
        return this;
    }
    public CommandNode end(Consumer<Context> in, int level){
        executable=in;
        OperatorLevel operatorLevel = OperatorLevel.ADMIN;
        Predicate<LoginStatus> predicate=(status)-> {
            return LoginStatus.hasPermissionLevel(level);
        };
        hasPower=predicate;
        return this;
    }
    public CommandNode end(Consumer<Context> in, Predicate<LoginStatus> predicate){
        executable=in;
        hasPower=predicate;
        return this;
    }
    public CommandNode progress(String in){
        if(isParameterStarter){
            return progress();
        }else{
            return children.containsKey(in)? children.get(in):null;
        }
    }
    public CommandNode progress(){
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