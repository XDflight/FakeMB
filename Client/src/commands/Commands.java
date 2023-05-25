package commands;

import commandCore.Command;
import commandCore.CommandInput;
import commandCore.Context;
import commands.TeacherCommand;
import commands.UserCommand;

import java.util.ArrayList;

public class Commands {

    public static Command rootCommand=new Command();
    static{
        TeacherCommand.register(rootCommand);
        UserCommand.register(rootCommand);
    }
    public static void parseCommand(ArrayList<String> params){
        Command commandProcedure=rootCommand;
        Context parameter=new Context();
        for (String param:
                params) {
            System.out.println("1:"+commandProcedure);
            commandProcedure=commandProcedure.progress(param);
            System.out.println("2:"+commandProcedure);
            if(commandProcedure==null){
                break;
            }
            if(commandProcedure instanceof CommandInput){
                parameter.add(commandProcedure.name,((CommandInput) commandProcedure).type,param);
            }
//            System.out.println("Procedure: "+commandProcedure.name);
            if(commandProcedure.isEnd()){
                commandProcedure.run(parameter);
            }
        }
        if(commandProcedure==null){
            System.out.println("Command non-existance");
            return;
        }
        if(!commandProcedure.isEnd()){
            System.out.println("Command is incomplete");
            return;
        }
    }
}
