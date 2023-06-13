package server.structs;

import security.OperatorLevel;
import server.structs.annotations.HashElement;
import server.structs.annotations.LoginRequired;
import server.structs.annotations.RegisterRequired;
import server.structs.annotations.UUID;

public class AccountData extends DataClass {
    @UUID
    @RegisterRequired
    @LoginRequired
    public String userName;
    @HashElement(hashType = "SHA-256")
    @UUID
    @RegisterRequired
    @LoginRequired
    public String hashPass;
    @UUID
    @RegisterRequired
    public String school_id;

//    public OperatorLevel opLevel;
}
