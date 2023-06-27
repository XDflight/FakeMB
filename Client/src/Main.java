import commandNodes.*;
import db.Database;
import server.SearchGroup;
import server.structs.AccountData;
import server.structs.CourseData;
import server.structs.DataClass;
import server.structs.PersonaData;
import util.Logger;
import util.StringHelper;
import server.DataCentral;

import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {
    public static Database db = new Database();

    static CommandNode rootCommandNode = new CommandNode();
    static Logger LOGGER = new Logger();


    public static void initialize() {
        DataCentral.registerDataType(AccountData.class,true);
        DataCentral.registerDataType(PersonaData.class);
        DataCentral.registerDataType(CourseData.class);
        DataCentral.loadDB();
    }

    public static void runCommand(String command) {
        parseCommand(StringHelper.breakDownString(command));
    }

    public static void main(String[] args) {
//        Debugger.runTest(1);
        initialize();
        Scanner puller = new Scanner(System.in);
        System.out.println("sponsored by linus tech tips.");
//        runCommand("register via AccountData superAdmin 2048");
//        runCommand("filter AccountData userName:superAdmin");
//        runCommand("edit AccountData isSuperAdmin:true");
//
//
//        runCommand("register via AccountData jameres 2048");
//        runCommand("register via PersonaData 2124 james male student");
//        runCommand("filter AccountData userName:jameres");
//
//        runCommand("edit AccountData persona:2124");
        runCommand("add Persona id=1");
        runCommand("add Persona id=2");
        runCommand("add Persona id=3");
        runCommand("add Course id=1 students=1,2,3");

        runCommand("dumpData");
        runCommand("filter Course id=1");
        runCommand("remove Persona id=1");
//        runCommand("dumpData");
//
//        runCommand("system db save");

//        System.out.println("SearchGroup.filteredGroup");
//        System.out.println(SearchGroup.filteredGroup);

        /*
         * <Command List>
         * - register via TableName(omit "Data") *Ordered Data*
         * - login via Account username password
         * - filter TableName(omit "Data") DataType:Data
         * - edit TableName(omit "Data") DataType:Data
         * - system db save
         * - add TableName(omit "Data") *Tagged Data*
         * - dumpData
         * - remove TableName(omit "Data") DataType:Data
         * - user bind student_id //Bugged
         *
         * //TODO
         *    1. quote mark fix for Tag-like info
         */

        while (true) {
            String filterGroupTxt = "";
            if (SearchGroup.filteredGroup != null && SearchGroup.filteredGroup.size() > 0) {
                filterGroupTxt +=
                        SearchGroup.filteredGroup.values().toArray()[0].getClass().getSimpleName()
                                + ":"
                                + ((DataClass)SearchGroup.filteredGroup.values().toArray()[0]).getUUID();
            }
            System.out.print("/" + filterGroupTxt + "> ");
            String userInput = puller.nextLine();
            parseCommand(StringHelper.breakDownString(userInput));
        }
    }
}
