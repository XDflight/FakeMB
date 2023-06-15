package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.OperatorLevel;

public class TeacherCommand {
    static CommandNode teacherCommandNode = new CommandNodeFork("teacher")
            .then(new CommandNodeFork("login"))
            .then(new CommandNodeFork("register"))
            .then(new CommandNodeFork("assess")
                    .then(new CommandNodeInput("courseId", "String")
                            .then(new CommandNodeInput("studentId", "String")
                                    .then(new CommandNodeInput("studentScore", "Integer")
                                            .then(new CommandNodeInput("fullScore", "Integer")
                                                    .end(context -> {

                                                    }, OperatorLevel.TEACHER)
                                            )
                                    )
                            )
                    )
            );

    public static void register(CommandNode root) {
        root.then(teacherCommandNode);
    }
}
