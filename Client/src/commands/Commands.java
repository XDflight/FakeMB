package commands;

import commandNodes.*;
import security.HashTool;

import java.util.ArrayList;

public class Commands {

    public static CommandNode rootCommandNode = new CommandNodeFork("root");

    static {
        TeacherCommand.register(rootCommandNode);
        UserCommand.register(rootCommandNode);
        SystemCommand.register(rootCommandNode);
    }

    public static void parseCommand(ArrayList<String> params) {
        CommandNode commandNode = rootCommandNode;
        Context parameter = new Context();
        double length=params.size();
        for (int i = 0; i < length; i++) {
            commandNode = commandNode.progress(params.get(0));
            if (commandNode == null) {
                break;
            }
            //debug message
//            System.out.println(commandNode.toStringTop());
            if (commandNode instanceof CommandNodeTags) {
                for (int j = 0; j < params.size(); j++) {
                    String[] strSegments = params.get(j).split("=");
                    String[] strSegments2 = params.get(j).split(":");
                    if(Math.max(strSegments2.length,strSegments.length)<2){
                        System.out.println("wrong format for tag No:"+(j+1));
                        continue;
                    }else{
                        if(strSegments2.length> strSegments.length){
                            parameter.add(strSegments2[0],"String",strSegments2[1]);
                        }else if(strSegments2.length < strSegments.length){
                            parameter.add(strSegments[0],"String",strSegments[1]);
                        }else{
                            parameter.add(strSegments[0],"String",strSegments[1]);
                        }
                    }

                }
            }
            if (commandNode instanceof CommandNodeInput) {
                String dataProtectionMethod=((CommandNodeInput) commandNode).hashType;
                parameter.add(
                        commandNode.name,
                        ((CommandNodeInput) commandNode).type,
                        dataProtectionMethod==null?
                                params.get(0)
                                :
                                HashTool.generate(params.get(0),dataProtectionMethod)
                );
            }
            if (commandNode.isEnd()) {
                commandNode.run(parameter);
                break;
            }
            params.remove(0);
        }
        if (commandNode == null) {
            System.out.println("Command non-existance");
            return;
        }
        if (!commandNode.isEnd()) {
            System.out.println("Command is incomplete");
            return;
        }
    }
}
