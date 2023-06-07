package db;

import javafx.util.Pair;
import util.Base64Helper;

public class Types {
    private static final Object[] supportedTypes = {
            new Pair<Class<?>, String>(Integer.class, "Integer"),
            new Pair<Class<?>, String>(String.class, "String")
    };

    /*
     * @public:     isSupported
     * @note:       Use this method to check if a data-type is supported by the database.
     * @param:      [(Class<?>) type] The class of the data-type.
     * @retval:     [boolean] True if the data-type is supported.
     */
    public static boolean isSupported(Class<?> type) {
        System.out.println(type);
        for (Object typePair : supportedTypes) {
            if (((Pair<Class<?>, String>) typePair).getKey() == type)
                return true;
        }
        return false;
    }

    /*
     * @public:     isSupported
     * @note:       Use this method to check if a data-type is supported by the database.
     * @param:      [(String) type] The string-style data-type.
     * @retval:     [boolean] True if the data-type is supported.
     */
    public static boolean isSupported(String type) {
        for (Object typePair : supportedTypes) {
            if (((Pair<Class<?>, String>) typePair).getValue().equalsIgnoreCase(type))
                return true;
        }
        return false;
    }

    /*
     * @public:     cls2str
     * @note:       This method can be used to convert a class-style type to a string-style type.
     * @param:      [(Class<?>) type] The class-style data-type to be converted.
     * @retval:     [String] The result string-style data-type.
     */
    public static String cls2str(Class<?> type) {
        for (Object typePair : supportedTypes) {
            if (((Pair<Class<?>, String>) typePair).getKey() == type)
                return ((Pair<Class<?>, String>) typePair).getValue();
        }
        return null;
    }

    /*
     * @public:     str2cls
     * @note:       This method can be used to convert a string-style type to a class-style type.
     * @param:      [(String) type] The string-style data-type to be converted.
     * @retval:     [Class<?>] The result class-style data-type.
     */
    public static Class<?> str2cls(String type) {
        for (Object typePair : supportedTypes) {
            if (((Pair<Class<?>, String>) typePair).getValue().equalsIgnoreCase(type))
                return ((Pair<Class<?>, String>) typePair).getKey();
        }
        return null;
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
