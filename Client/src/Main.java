import commandCore.*;
import util.Logger;
import util.StringHelper;

import java.util.ArrayList;
import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {

    static Command rootCommand=new Command();
    static Logger LOGGER=new Logger();
    public static void main(String[] args) {
        Scanner puller=new Scanner(System.in);

        while(true) {
            String userInput = puller.nextLine();
            ArrayList<String> params = StringHelper.breakDownString(userInput);
            parseCommand(StringHelper.breakDownString(userInput));
        }
    }


}
