package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTool {

    public static String defaultHashAlgorith="MD5";
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
     * @public:     generate
     * @note:       This method can be used to calculate the hash value of a string.
     *              The hash algorithm implemented is secure enough to store private and sensitive messages.
     *              The hash value is so big so that it is returned as a string instead of a number.
     * @param:      [(String) msg] The message to be hashed.
     * @retval:     [String] The hash value for the message.
     */
    public static String generate(String msg,String hashType) {
        MessageDigest hashInst = getHashInst(hashType);
        if (hashInst == null)
            return null;
        return new BigInteger(1,
                hashInst.digest(msg.getBytes())
        ).toString(16).toUpperCase();
    }

    private static MessageDigest getHashInst() {
       return getHashInst(defaultHashAlgorith);
    }

    private static MessageDigest getHashInst(String hashType) {
        try {
            return MessageDigest.getInstance(hashType);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
