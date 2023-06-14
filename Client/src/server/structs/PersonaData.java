package server.structs;

import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

public class PersonaData extends DataClass {
    @RegisterRequired
    @UUID
    public String school_id;

    @RegisterRequired
    public String name;

    @RegisterRequired
    public String gender;

    @RegisterRequired
    public String role;
}
