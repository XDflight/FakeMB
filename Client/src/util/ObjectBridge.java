package util;

import server.structs.DataClass;
import server.structs.annotations.Ref;
import server.structs.annotations.RefList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectBridge {
    public static DataClass rowToEntry(Class<?> classIn, Map<String,Object> row){
        DataClass entry= (DataClass) ReflectHelper.classInstance(classIn);
        return entry.readRow(row);
    }
    public static Map<String, Object> entryToRow(Class<?> classIn, DataClass entry){
        return entry.toRow();
    }
}
