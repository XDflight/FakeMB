package server.structs;

import server.structs.annotations.ComplexData;
import server.structs.annotations.RefList;
import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

import java.util.ArrayList;

@ComplexData
public class CourseData extends DataClass {
    @UUID
    @RegisterRequired
    public String courseId;

    @RegisterRequired
    public String courseName;

    @RefList(classType = PersonaData.class)
    public ArrayList<PersonaData> students;
}
