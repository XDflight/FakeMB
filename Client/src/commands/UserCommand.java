package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.LoginStatus;
import server.DataManager;
import server.structs.PersonaData;

import static commands.SystemCommand.saveDbChanges;
import static server.DataCentral.dataManagers;

public class UserCommand {
    static CommandNode userCommandNode = new CommandNodeFork("user")

            .then(new CommandNodeFork("logout")
                    .end(context -> {
                        LoginStatus.setUser(null);
                        System.out.println("Logged out.");
                    }, 0)
            )
            .then(new CommandNodeFork("bind").then(new CommandNodeInput("persona","String").end((context)->{
                    if(LoginStatus.loggedIn()){

                        DataManager manager=dataManagers.get(PersonaData.class);
                        LoginStatus.getUser().persona= (PersonaData) manager.getEntry(context.get("persona"));
                        saveDbChanges();

                    }

            })))
            .then(new CommandNodeFork("login")
                    .then(new CommandNodeFork("status")
                            .end(context -> {
                                if (LoginStatus.loggedIn()) {
                                    System.out.println("[Status] Already Logged in");
                                    System.out.println("Current User Name: " + LoginStatus.getUname());
                                } else
                                    System.out.println("[Status] Haven't logged in.");

                                System.out.println("Current Permission Level: " + LoginStatus.getPermissionLevel());
                            }, 0)
                    )
            );


    public static void register(CommandNode root) {
        root.then(userCommandNode);
    }

    public static void registerAccount(String account, String password) {
        System.out.println("Registered Account " + account + " with password " + password);
    }
}
