package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import commandNodes.Context;

import java.util.ArrayList;

public class Commands {

    public static CommandNode rootCommandNode =new CommandNodeFork("root");
    static{
        TeacherCommand.register(rootCommandNode);
        UserCommand.register(rootCommandNode);
    }
    public static void parseCommand(ArrayList<String> params){
        CommandNode commandNode = rootCommandNode;
        Context parameter=new Context();
        for (String param:
                params) {
            commandNode = commandNode.progress(param);
            if(commandNode ==null){
                break;
            }
//            System.out.println(commandNode.toStringTop());
            if(commandNode instanceof CommandNodeInput){
                parameter.add(commandNode.name,((CommandNodeInput) commandNode).type,param);
            }
            if(commandNode.isEnd()){
                commandNode.run(parameter);
                break;
            }
        }
        if(commandNode ==null){
            System.out.println("Command non-existance");
            return;
        }
        if(!commandNode.isEnd()){
            System.out.println("Command is incomplete");
            return;
        }
    }
}
