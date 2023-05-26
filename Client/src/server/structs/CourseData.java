package server.structs;

import server.structs.annotations.CSV;

import java.util.ArrayList;

public class CourseData {
    @CSV
    ArrayList<PersonaData> students;
}
