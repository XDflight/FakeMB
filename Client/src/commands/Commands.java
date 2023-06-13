package commands;

import commandNodes.*;

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
                    String[] strSegments = params.get(j).split(":");
                    parameter.add(strSegments[0],"String",strSegments[1]);
                }
            }
            if (commandNode instanceof CommandNodeInput) {
                parameter.add(commandNode.name, ((CommandNodeInput) commandNode).type, params.get(0));
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
