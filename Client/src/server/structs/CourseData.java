package server.structs;

import server.structs.annotations.ComplexData;
import server.structs.annotations.RefMap;
import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

import java.util.Map;

@ComplexData
public class CourseData extends DataClass {
    @UUID
    @RegisterRequired
    public String courseId;

    @RegisterRequired
    public String courseName;

    public String courseInfo;

    @RefMap(classType = PersonaData.class)
    public Map<String,PersonaData> students;
}
