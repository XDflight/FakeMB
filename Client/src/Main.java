import commandCore.*;
import systematic.School;
import systematic.Student;
import util.StringHelper;

import java.util.ArrayList;
import java.util.Scanner;

import static commands.Commands.parseCommand;

public class Main {

    static Command rootCommand=new Command();
    public static void main(String[] args) {
        School school=new School();
        Student a=new Student();
        school.studentsAtlas.add(a);
        school.studentsAtlas.remove(a);

//        System.out.println("hello, user");
//        Scanner puller=new Scanner(System.in);
//
//
//
//
//        while(true){
//            String userInput=puller.nextLine();
//            parseCommand(StringHelper.breakDownString(userInput));
//        }
    }


}
