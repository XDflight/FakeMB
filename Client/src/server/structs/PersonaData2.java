package server.structs;

import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

public class PersonaData2 extends DataClass {
    @RegisterRequired
    @UUID
    public String id;

    @RegisterRequired
    public String name;

    @RegisterRequired
    public String gender;

    @RegisterRequired
    public String userGroup;
}
