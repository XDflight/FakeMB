import core.Command;
import core.CommandFork;
import core.CommandInput;
import core.Context;

import java.util.Scanner;

public class Main {
    public static void register(String account,String password){
        System.out.println("Registered Account "+account+" with password "+password);
    }
    public static void main(String[] args) {
        System.out.println("hello, user");

        Command commandBuild=new Command();
        commandBuild
            .then(new CommandFork("user")
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
                    .then(new CommandFork("register"))
            )
            .then(new CommandFork("teacher")
                    .then(new CommandFork("login"))
                    .then(new CommandFork("register"))
            );


        String command=new Scanner(System.in).nextLine();
        String copy=command;
        Command commandProcedure=commandBuild;
        Context parameter=new Context();
        while( copy.length()>0 ){
            if(copy.charAt(0)==' '){
                copy=copy.substring(1);
                continue;
            }else{
                int keyFrame=copy.indexOf(' ')==-1?copy.length():copy.indexOf(' ');
                String parsed=copy.substring(0,keyFrame);
                System.out.println("Parsed: "+parsed);

                commandProcedure=commandProcedure.progress(parsed);
                if(commandProcedure instanceof CommandInput){
                    parameter.add(((CommandInput) commandProcedure).name,((CommandInput) commandProcedure).type,parsed);
                }
                System.out.println("Procedure: "+commandProcedure.name);
                if(!commandProcedure.hasFork()){
                    commandProcedure.executable.accept(parameter);
                }

                copy=copy.substring(keyFrame);
            }
        }
        if(commandProcedure.hasFork()){
            System.out.println("命令不完整！");
        }
    }
}
