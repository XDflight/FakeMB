package util;

import javafx.util.Pair;

import java.util.List;

public class GradeCalc {
    private static final int MAX_GRADE = 100;

    private static final Object[] classGradeLevels = {
            new Pair<Integer, String>(97, "A+"),
            new Pair<Integer, String>(90, "A"),
            new Pair<Integer, String>(80, "B"),
            new Pair<Integer, String>(70, "C"),
            new Pair<Integer, String>(60, "D"),
            new Pair<Integer, String>(0, "F")
    };

    private static final Object[] GPALevels = {
            new Pair<String, Float>("A+", 4.3f),
            new Pair<String, Float>("A", 4f),
            new Pair<String, Float>("B", 3f),
            new Pair<String, Float>("C", 2f),
            new Pair<String, Float>("D", 1f),
            new Pair<String, Float>("F", 0f)
    };

    public static String calcClassGrade(List<Integer> grades) {
        if (grades.size() == 0)
            return null;
        float gradeSum = 0;
        for (int grade : grades) {
            gradeSum += (float) grade / MAX_GRADE;
        }
        gradeSum /= grades.size();
        gradeSum = Math.round(gradeSum * 100) / 100f;
        for (Object lvlObj : classGradeLevels) {
            Pair<Integer, String> lvl = (Pair<Integer, String>) lvlObj;
            if (gradeSum >= ((float) lvl.getKey() / MAX_GRADE)) {
                return lvl.getValue();
            }
        }
        return null;
    }

    public static float calcOverallGPA(Object[] gradesList) {
        float overallGPA = 0;
        int countedNum = 0;
        for (Object grades : gradesList) {
            String classGrade = calcClassGrade((List<Integer>) grades);
            if (classGrade != null) {
                for (Object lvlObj : GPALevels) {
                    Pair<String, Float> lvl = (Pair<String, Float>) lvlObj;
                    if (lvl.getKey().equals(classGrade)) {
                        overallGPA += lvl.getValue();
                        countedNum++;
                    }
                }
            }
        }
        if (countedNum == 0)
            return 0;
        return Math.round(overallGPA / countedNum * 100) / 100f;
    }
}
