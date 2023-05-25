package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;

public class UserCommand {
    static CommandNode userCommandNode =new CommandNodeFork("user")

            .then(new CommandNodeFork("register")
                    .then(new CommandNodeInput("account","String")
                            .then(new CommandNodeInput("password","String")
                                    .end(context -> {
                                            registerAccount(
                                                    (String)context.get("account"),
                                                    (String)context.get("password")
                                            );},
                                            0
                                    )
                            )
                    )
            );


    public static void register(CommandNode root){
        root.then(userCommandNode);
    }
    public static void registerAccount(String account,String password){
        System.out.println("Registered Account "+account+" with password "+password);
    }
}
