package server.structs;

import server.structs.annotations.HashElement;
import server.structs.annotations.UUID;

public class AccountData extends DataClass {
    @UUID
    public String userName;
    @HashElement(hashType = "SHA-256")
    public String hashPass;
    public String school_id;
}
