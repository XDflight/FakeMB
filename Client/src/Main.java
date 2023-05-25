import commandCore.*;
import db.Database;
import security.OperatorLevel;
import systematic.School;
import systematic.Student;
import util.Logger;
import util.StringHelper;
import visualize.DataCentral;

import java.util.ArrayList;
import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {
    public static Database db = new Database();

    static Command rootCommand = new Command();
    static Logger LOGGER = new Logger();

    static DataCentral dataCentral=new DataCentral();

    public static void main(String[] args) {
        Scanner puller = new Scanner(System.in);

        while (true) {
            String userInput = puller.nextLine();
            ArrayList<String> params = StringHelper.breakDownString(userInput);
            parseCommand(StringHelper.breakDownString(userInput));
        }

    }


}
