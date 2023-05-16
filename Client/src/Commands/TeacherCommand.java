package Commands;

import CommandCore.Command;
import CommandCore.CommandFork;

import static CommandCore.CommandDispatcher.rootCommand;

public class TeacherCommand {
    static Command teacherCommand=new CommandFork("teacher")
            .then(new CommandFork("login"))
            .then(new CommandFork("register"));
    public static void register(){
        rootCommand.then(teacherCommand);
    }
}
