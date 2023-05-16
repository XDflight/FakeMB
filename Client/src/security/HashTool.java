package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTool {
    public static String generate(String msg) {
        MessageDigest hashInst = getHashInst();
        if (hashInst == null)
            return null;
        return new BigInteger(1,
                hashInst.digest(msg.getBytes())
        ).toString(16).toUpperCase();
    }

    private static MessageDigest getHashInst() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
