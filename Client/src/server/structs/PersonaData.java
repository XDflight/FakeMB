package server.structs;

import server.structs.annotations.RefMap;
import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

import java.util.Map;

public class PersonaData extends DataClass {
    @RegisterRequired
    @UUID
    public String id;

    @RegisterRequired
    public String name;

    @RegisterRequired
    public String gender;

    @RegisterRequired
    public String userGroup;

    @RefMap(classType = GradeData.class)
    public Map<String, GradeData> grades;
}
