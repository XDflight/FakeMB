package util;

import server.structs.DataClass;

import java.util.Map;

public class ObjectBridge {
    public static DataClass rowToEntry (Class<? extends DataClass> classIn, Map<String,Object> row){
        return ((DataClass)ReflectHelper.classInstance(classIn)).readRow(row);
    }
    public static Map<String, Object> entryToRow (Class<?> classIn, DataClass entry){
        return entry.toRow();
    }
}
