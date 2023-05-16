import CommandCore.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Command rootCommand=new Command();
    public static void main(String[] args) {
        System.out.println("hello, user");
        Scanner puller=new Scanner(System.in);




        while(true){
            String userInput=puller.nextLine();
            parseCommand(breakDownString(userInput));
        }
    }
    public static void parseCommand(ArrayList<String> params){
        Command commandProcedure=rootCommand;
        Context parameter=new Context();
        for (String param:
             params) {
            commandProcedure=commandProcedure.progress(param);
            if(commandProcedure instanceof CommandInput){
                parameter.add(commandProcedure.name,((CommandInput) commandProcedure).type,param);
            }
//            System.out.println("Procedure: "+commandProcedure.name);
            if(commandProcedure.isEnd()){
                commandProcedure.executable.accept(parameter);
            }
        }
        if(!commandProcedure.isEnd()){
            System.out.println("命令不完整！");
        }
        //特有的叹号
    }
    public static ArrayList<String> breakDownString(String rawString){
        ArrayList<String>atoms=new ArrayList<>();
        while( rawString.length()>0 ){
            if(rawString.charAt(0)==' '){
                rawString=rawString.substring(1);
                continue;
            }else{
                int keyFrame=rawString.indexOf(' ')==-1?rawString.length():rawString.indexOf(' ');
                String piece=rawString.substring(0,keyFrame);

                atoms.add(piece);

                rawString=rawString.substring(keyFrame);
            }
        }
        return atoms;
    }
}
