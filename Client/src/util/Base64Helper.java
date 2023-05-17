package util;

import java.util.Base64;

public class Base64Helper {
    /*
     * @public:     encode
     * @note:       This method can be used to encode a byte string into Base64 format.
     * @param:      [(byte[]) data] The data to be encoded
     * @retval:     [String] The Base64 result.
     */
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /*
     * @public:     decode
     * @note:       This method can be used to decode a Base64 format string into original byte string.
     * @param:      [(String) str] The Base64 format string.
     * @retval:     [byte[]] The byte string result.
     */
    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }
}
