import commandCore.*;
import security.OperatorLevel;
import systematic.School;
import systematic.Student;
import util.StringHelper;

import java.util.ArrayList;
import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {

    static Command rootCommand=new Command();
    public static void main(String[] args) {
        Scanner puller=new Scanner(System.in);




        while(true){
            String userInput=puller.nextLine();
            parseCommand(StringHelper.breakDownString(userInput));
        }

    }


}
