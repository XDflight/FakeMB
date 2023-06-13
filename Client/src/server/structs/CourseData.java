package server.structs;

import server.structs.annotations.ComplexData;
import server.structs.annotations.RefList;

import java.util.ArrayList;

@ComplexData
public class CourseData {
    @RefList(classType = PersonaData.class)
    ArrayList<PersonaData> students;
}
