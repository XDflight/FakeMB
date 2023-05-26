package server.structs;

import server.structs.annotations.HashElement;

public class AccountData extends dataClass{
    public String userName;
    @HashElement(hashType = "SHA-256")
    public String hashPass;
    public String school_id;
}
