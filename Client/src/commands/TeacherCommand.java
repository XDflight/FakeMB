package commands;

import commandCore.Command;
import commandCore.CommandFork;

public class TeacherCommand {
    static Command teacherCommand=new CommandFork("teacher")
            .then(new CommandFork("login"))
            .then(new CommandFork("register"));
    public static void register(Command root){
        root.then(teacherCommand);
    }
}
