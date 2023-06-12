package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.LoginStatus;

public class UserCommand {
    static CommandNode userCommandNode = new CommandNodeFork("user")

            .then(new CommandNodeFork("register")
                    .then(new CommandNodeInput("account", "String")
                            .then(new CommandNodeInput("password", "String")
                                    .end(context -> {
                                                registerAccount(
                                                        (String) context.get("account"),
                                                        (String) context.get("password")
                                                );
                                            },
                                            0
                                    )
                            )
                    )
            ).then(new CommandNodeFork("login")
                    .then(new CommandNodeFork("status")
                            .end(context -> {
                                if (LoginStatus.getPermissionLevel() > 0) {
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
