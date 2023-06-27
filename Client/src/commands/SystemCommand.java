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
                    .then(new CommandNodeFork("useSystem")
                            .then(new CommandNodeInput("system","String")
                                    .end(context -> {
                                        DataCentral.portStorageTo((String)context.get("system"));
                                    },0)
                            )
                    )
            );
    static CommandNode exitNode = new CommandNodeFork("exit")
            .end(context -> {
                System.exit(-1);
            }, 0);
    static CommandNode dumpDataNode = new CommandNodeFork("dumpData")
            .end(context -> {
                DataCentral.printDataset();
            }, 0);



    public static void register(CommandNode root) {
        root.then(commandNode);
        root.then(exitNode);
        root.then(dumpDataNode);
    }

    public static void saveDbChanges() {
        DataCentral.saveToStorage();
    }
}
