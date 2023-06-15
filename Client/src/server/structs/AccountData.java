package server.structs;

import security.OperatorLevel;
import server.structs.annotations.*;

@ComplexData
public class AccountData extends DataClass {
    @UUID
    @RegisterRequired
    @LoginRequired
    public String userName;
    @HashElement(hashType = "SHA-256")
    @RegisterRequired
    @LoginRequired
    public String hashPass;
    @Ref(classType = PersonaData.class)
    public PersonaData persona;
    public String isSuperAdmin;
}
