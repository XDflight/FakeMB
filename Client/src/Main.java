import commandNodes.*;
import db.Database;
import server.structs.AccountData;
import server.structs.PersonaData;
import util.Logger;
import util.StringHelper;
import server.DataCentral;

import java.util.Scanner;

import static commands.Commands.parseCommand;
import static server.DataCentral.registerDataType;

public class Main {
    public static Database db = new Database();

    static CommandNode rootCommandNode = new CommandNode();
    static Logger LOGGER = new Logger();


    public static void initialize(){
        DataCentral.registerDataType(AccountData.class);
        DataCentral.registerDataType(PersonaData.class);
    }

    public static void main(String[] args) {
        initialize();
        Scanner puller = new Scanner(System.in);
        System.out.println("Welcome to stupidity shell.");
        while (true) {
            String userInput = puller.nextLine();
//            ArrayList<String> params = StringHelper.breakDownString(userInput);
            parseCommand(StringHelper.breakDownString(userInput));
        }

    }


}
