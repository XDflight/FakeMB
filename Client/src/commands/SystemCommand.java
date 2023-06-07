package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import commandNodes.CommandNodeTags;
import server.DataCentral;
import server.SearchGroup;

public class SystemCommand {
    static CommandNode commandNode = new CommandNodeFork("system")
            .then(new CommandNodeFork("db")
                    .then(new CommandNodeFork("save")
                            .end(context -> {
                                saveDbChanges();
                            }, 0)
                    )
            );
    static CommandNode exitNode = new CommandNodeFork("exit")
                            .end(context -> {
                                System.exit(-1);
                            }, 0);



    public static void register(CommandNode root) {
        root.then(commandNode);
        root.then(exitNode);
    }

    public static void saveDbChanges() {
        DataCentral.saveChanges();
    }
}
