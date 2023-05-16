package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTool {
    /*
     * @public:     generate
     * @note:       This method can be used to calculate the hash value of a string.
     *              The hash algorithm implemented is secure enough to store private and sensitive messages.
     *              The hash value is so big so that it is returned as a string instead of a number.
     * @param:      [(String) msg] The message to be hashed.
     * @retval:     [String] The hash value for the message.
     */
    public static String generate(String msg) {
        MessageDigest hashInst = getHashInst();
        if (hashInst == null)
            return null;
        return new BigInteger(1,
                hashInst.digest(msg.getBytes())
        ).toString(16).toUpperCase();
    }

    /*
     * @private:    getHastInst
     * @note:       This method can be used to get a hashing-instance from the system JDK.
     * @retval:     [MessageDigest] A system-provide hashing instance.
     */
    private static MessageDigest getHashInst() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
