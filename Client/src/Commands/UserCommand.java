package Commands;

import CommandCore.Command;
import CommandCore.CommandFork;
import CommandCore.CommandInput;

public class UserCommand {
    static Command userCommand=new CommandFork("user")
            .then(new CommandFork("login")
                    .then(new CommandInput("account","String")
                            .then(new CommandInput("password","String")
                                    .end(context -> {
                                        register(
                                                (String)context.get("account"),
                                                (String)context.get("password")
                                        );
                                    })
                            )
                    )
            )
            .then(new CommandFork("register"));

    public static void register(String account,String password){
        System.out.println("Registered Account "+account+" with password "+password);
    }
}
