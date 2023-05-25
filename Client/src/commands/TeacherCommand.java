package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;

public class TeacherCommand {
    static CommandNode teacherCommandNode =new CommandNodeFork("teacher")
            .then(new CommandNodeFork("login"))
            .then(new CommandNodeFork("register"));
    public static void register(CommandNode root){
        root.then(teacherCommandNode);
    }
}
