package server.structs;

import server.structs.annotations.ComplexData;
import server.structs.annotations.Ref;
import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

import java.util.Map;

@ComplexData
public class GradeData extends DataClass {
    @UUID
    String id;

    @RegisterRequired
    public String grade;

    @Ref(classType = CourseData.class)
    @RegisterRequired
    public CourseData course;

    @Ref(classType = PersonaData.class)
    @RegisterRequired
    public PersonaData persona;
}
