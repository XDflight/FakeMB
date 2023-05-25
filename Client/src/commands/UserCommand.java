package commands;

import commandCore.Command;
import commandCore.CommandFork;
import commandCore.CommandInput;
import server.structs.AccountData;

import java.net.CookieHandler;

public class UserCommand {
    static Command userCommand=new CommandFork("user")

            .then(new CommandFork("register")
                    .then(new CommandInput("account","String")
                            .then(new CommandInput("password","String")
                                    .end(context -> {
                                            registerAccount(
                                                    (String)context.get("account"),
                                                    (String)context.get("password")
                                            );},
                                            0
                                    )
                            )
                    )
            )
            .then(Command.dataOperation(AccountData.class,true));


    public static void register(Command root){
        root.then(userCommand);
    }
    public static void registerAccount(String account,String password){
        System.out.println("Registered Account "+account+" with password "+password);
    }
}
