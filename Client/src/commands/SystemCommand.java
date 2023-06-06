package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import server.DataCentral;

public class SystemCommand {
    static CommandNode commandNode = new CommandNodeFork("system")
            .then(new CommandNodeFork("db")
                    .then(new CommandNodeFork("save")
                            .end(context -> {
                                saveDbChanges();
                            }, 0)
                    )
            );

    public static void register(CommandNode root) {
        root.then(commandNode);
    }

    public static void saveDbChanges() {
        DataCentral.saveChanges();
    }
}
