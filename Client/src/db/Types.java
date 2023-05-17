package db;

import util.Base64Helper;

public class Types {
    private static final Class<?>[] supportedTypes = {
            Integer.class,
            String.class
    };

    /*
     * @public:     isSupported
     * @note:       Use this method to check if a data-type is supported by the database.
     * @param:      [(Class<?>) type] The class of the data-type.
     * @retval:     [boolean] True if the data-type is supported.
     */
    public static boolean isSupported(Class<?> type) {
        for (Class<?> cls : supportedTypes) {
            if (cls == type)
                return true;
        }
        return false;
    }

    /*
     * @public:     serialize
     * @note:       Use this method to serialize an object.
     * @param:      [(Object) obj] The object to be serialized.
     * @param:      [(Class<?>) cls] The class of the data-type.
     * @retval:     [String] The serialization result.
     */
    public static String serialize(Object obj, Class<?> cls) {
        if (cls == Integer.class)
            return IntegerType.serialize(obj);
        if (cls == String.class)
            return StringType.serialize(obj);
        return null;
    }

    /*
     * @public:     deserialize
     * @note:       Use this method to deserialize an object.
     * @param:      [(String) msg] The string to be deserialized.
     * @param:      [(Class<?>) cls] The class of the data-type.
     * @retval:     [Object] The object retrieved.
     */
    public static Object deserialize(String msg, Class<?> cls) {
        if (cls == Integer.class)
            return IntegerType.deserialize(msg);
        if (cls == String.class)
            return StringType.deserialize(msg);
        return null;
    }

    private static class IntegerType {
        public static String serialize(Object obj) {
            return ((Integer) obj).toString();
        }

        public static Object deserialize(String msg) {
            return Integer.parseInt(msg);
        }
    }

    private static class StringType {
        public static String serialize(Object obj) {
            return Base64Helper.encode(((String) obj).getBytes());
        }

        public static Object deserialize(String msg) {
            return new String(Base64Helper.decode(msg));
        }
    }
}
