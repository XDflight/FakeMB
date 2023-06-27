package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.LoginStatus;
import security.OperatorLevel;
import server.DataCentral;
import server.DataManager;
import server.SearchGroup;
import server.structs.PersonaData;

import java.awt.event.PaintEvent;

import static commands.SystemCommand.saveDbChanges;
import static server.DataCentral.dataManagers;

public class UserCommand {
    static CommandNode userCommandNode = new CommandNodeFork("user")

            .then(new CommandNodeFork("logout")
                    .end(context -> {
                        LoginStatus.setUser(null);
                        System.out.println("Logged out.");
                    }, 0)
            ).then(new CommandNodeFork("bind").then(new CommandNodeInput("persona","String").end((context)->{
                    if(LoginStatus.loggedIn()){

                        DataManager manager=dataManagers.get(PersonaData.class);
                        LoginStatus.getUser().persona= (PersonaData) manager.getByUUID(context.get("persona"));
                        saveDbChanges();

//                        DataManager manager=dataManagers.get(classIn);
//                        SearchGroup.filteredGroup.forEach((data)->{
//                            data.editBy(manager.rowToObject(context.parameters));
//                        });
//                        LoginStatus.getUser().editBy(rowToObject)
                    }

            })));


    public static void register(CommandNode root) {
        root.then(userCommandNode);
    }

    public static void registerAccount(String account, String password) {
        System.out.println("Registered Account " + account + " with password " + password);
    }
}
