import commandNodes.*;
import db.Database;
import server.SearchGroup;
import server.structs.AccountData;
import server.structs.PersonaData;
import tests.Debugger;
import util.Logger;
import util.StringHelper;
import server.DataCentral;

import java.util.Scanner;

import static commands.Commands.parseCommand;
import static server.DataCentral.dataManagers;
import static server.DataCentral.registerDataType;

public class Main {
    public static Database db = new Database();

    static CommandNode rootCommandNode = new CommandNode();
    static Logger LOGGER = new Logger();


    public static void initialize(){
        DataCentral.registerDataType(AccountData.class);
        DataCentral.registerDataType(PersonaData.class);

        DataCentral.loadDB();
    }
    public static void runCommand(String command){
        parseCommand(StringHelper.breakDownString(command));
    }
    public static void main(String[] args) {
//        Debugger.runTest(1);
        initialize();
        Scanner puller = new Scanner(System.in);
        System.out.println("sponsored by linus tech tips.");
        runCommand("register via AccountData superAdmin 2048");
        runCommand("register via AccountData 1 2");
        runCommand("filter AccountData userName:superAdmin");
        runCommand("edit AccountData isSuperAdmin:true");

//        runCommand("register via PersonaData 2124 female");

//        runCommand("system db save");
        DataCentral.printDataset();

//        System.out.println("SearchGroup.filteredGroup");
//        System.out.println(SearchGroup.filteredGroup);

        while (true) {
            String userInput = puller.nextLine();
            parseCommand(StringHelper.breakDownString(userInput));
        }
    }
}
