import commandCore.*;
import util.StringHelper;

import java.util.ArrayList;
import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {

    static Command rootCommand=new Command();
    public static void main(String[] args) {
        System.out.println("hello, user");
        Scanner puller=new Scanner(System.in);




        while(true){
            String userInput=puller.nextLine();
            parseCommand(StringHelper.breakDownString(userInput));
        }
    }


}
