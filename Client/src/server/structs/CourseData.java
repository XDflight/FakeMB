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
    public String id;

    @RegisterRequired
    public String name;

    public String info;

    @RefMap(classType = PersonaData.class)
    public Map<String,PersonaData> students;
}
