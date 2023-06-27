package util;

import server.structs.DataClass;

import java.util.Map;

public class OTBridgeUtil {
    public static DataClass rowToEntry (Class<? extends DataClass> classIn, Map<String,Object> row){
        return ((DataClass) ReflectionUtil.classInstance(classIn)).readRow(row);
    }
    public static Map<String, Object> entryToRow (Class<?> classIn, DataClass entry){
        return entry.toRow();
    }
}
