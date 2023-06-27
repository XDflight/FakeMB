package commands;

import commandNodes.CommandNode;
import commandNodes.CommandNodeFork;
import commandNodes.CommandNodeInput;
import security.LoginStatus;
import security.OperatorLevel;
import server.DataCentral;
import server.DataManager;
import server.structs.CourseData;
import server.structs.GradeData;
import server.structs.PersonaData;
import util.GradeCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static commands.SystemCommand.saveDbChanges;
import static server.DataCentral.dataManagers;

public class UserCommand {
    static CommandNode userCommandNode = new CommandNodeFork("user")

            .then(new CommandNodeFork("logout")
                    .end(context -> {
                        LoginStatus.setUser(null);
                        System.out.println("Logged out.");
                    }, 0)
            )
            .then(new CommandNodeFork("bind").then(new CommandNodeInput("persona", "String").end((context) -> {
                if (LoginStatus.loggedIn()) {

                    DataManager manager = dataManagers.get(PersonaData.class);
                    LoginStatus.getUser().persona = (PersonaData) manager.getEntry(context.get("persona"));
                    saveDbChanges();

                }

            })))
            .then(new CommandNodeFork("login")
                    .then(new CommandNodeFork("status")
                            .end(context -> {
                                if (LoginStatus.loggedIn()) {
                                    System.out.println("[Status] Already Logged in");
                                    System.out.println("Current User Name: " + LoginStatus.getUname());
                                } else
                                    System.out.println("[Status] Haven't logged in.");

                                System.out.println("Current Permission Level: " + LoginStatus.getPermissionLevel());
                            }, 0)
                    )
            )
            .then(new CommandNodeFork("grade")
                    .then(new CommandNodeFork("view")
                            .end(context -> {
                                PersonaData persona = LoginStatus.getUser().persona;
                                Map<String, List<Integer>> gradeMap = new HashMap<>();
                                GradeData[] gradeDataList = DataCentral.getDatasetOfClass(GradeData.class)
                                        .objectMap.values().toArray(new GradeData[0]);
                                for (GradeData gradeData : gradeDataList) {
                                    if (gradeData.persona == persona) {
                                        gradeMap.computeIfAbsent(gradeData.course.id, k -> new ArrayList<>());
                                        gradeMap.get(gradeData.course.id).add(Integer.valueOf(gradeData.grade));
                                    }
                                }
                                CourseData[] courseDataList = DataCentral.getDatasetOfClass(CourseData.class)
                                        .objectMap.values().toArray(new CourseData[0]);
                                List<List<Integer>> gradesList = new ArrayList<List<Integer>>();
                                System.out.println("====== Begin of Grade Report ======");
                                for (CourseData courseData : courseDataList) {
                                    if (courseData.students.containsValue(persona)) {
                                        System.out.println("\n--- Begin of Class \"" + courseData.name + "\" Score Report ---");
                                        if (!gradeMap.containsKey(courseData.id)) {
                                            System.out.println("No Available Scores for the Class!");
                                            continue;
                                        }
                                        List<Integer> grades = gradeMap.get(courseData.id);
                                        for (int i = 0; i < grades.size(); i++) {
                                            System.out.println("Assessment " + (i + 1) + ": " + grades.get(i));
                                        }
                                        gradesList.add(grades);
                                        System.out.println("Class Overall Grade: " + GradeCalc.calcClassGrade(grades));
                                        System.out.println("--- End of Class \"" + courseData.name + "\" Score Report ---\n");
                                    }
                                }
                                System.out.println("Overall GPA: "
                                        + GradeCalc.calcOverallGPA(gradesList.toArray()) + "\n");
                                System.out.println("====== End of Grade Report ======");
                            }, OperatorLevel.STUDENT)
                    )
            );


    public static void register(CommandNode root) {
        root.then(userCommandNode);
    }

    public static void registerAccount(String account, String password) {
        System.out.println("Registered Account " + account + " with password " + password);
    }
}
