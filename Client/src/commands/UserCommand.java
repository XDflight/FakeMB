package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.LoginStatus;
import security.OperatorLevel;

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
                                if (LoginStatus.loggedIn()) {
                                    System.out.println("[Status] Already Logged in");
                                    System.out.println("Current User Name: " + LoginStatus.getUname());
                                } else
                                    System.out.println("[Status] Haven't logged in.");

                                System.out.println("Current Permission Level: " + LoginStatus.getPermissionLevel());
                            }, 0)
                    )
            ).then(new CommandNodeFork("logout")
                    .end(context -> {
                        LoginStatus.setPermissionLevel(0);
                        LoginStatus.setUname(null);
                        System.out.println("Logged out.");
                    }, 0)
            ).then(new CommandNodeFork("assign")
                    .then(new CommandNodeFork("permission")
                            .then(new CommandNodeInput("uname","String")
                                    .then(new CommandNodeInput("permission","String")
                                            .end(context -> {

                                            }, OperatorLevel.ADMIN)
                                    )
                            )
                    )
            );


    public static void register(CommandNode root) {
        root.then(userCommandNode);
    }

    public static void registerAccount(String account, String password) {
        System.out.println("Registered Account " + account + " with password " + password);
    }
}
