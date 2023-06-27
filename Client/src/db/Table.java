package db;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Table {
    private String tableName;
    private Map<String, Class<?>> fields;
    private List<Map<String, Object>> rows;

    /*
     * @constructor
     * @note:       Construct a fresh clean Table.
     * @param:      [(String) tableName] The name of the table.
     */
    public Table(String tableName) {
        this.tableName = tableName;
        fields = new HashMap<String, Class<?>>();
        rows = new ArrayList<Map<String, Object>>();
    }

    /*
     * @constructor
     * @note:       Construct a Table from a serialized string;
     * @param:      [(int) tableId] The table ID.
     * @param:      [(String) str] The serialized string to be processed.
     */
    public Table(int tableId, String str) {
        deserialize(str, tableId, true, true);
    }

    /*
     * @public:     getTableName
     * @note:       Get the table's name.
     * @retval:     [String] The table's name.
     */
    public String getTableName() {
        return tableName;
    }

    /*
     * @public:     addField
     * @note:       Add a field(column) to the table.
     * @param:      [(String) fieldName] The field name.
     * @param:      [(Class<?>) cls] The class of the data-type of the field.
     * @retval:     [Table] "this" is returned.
     */
    public Table addField(String fieldName, Class<?> cls) {
        if (!Types.isSupported(cls)) {
            System.out.println("unsupported field type "+cls.toString()+" fall back to String.class");
            fields.put(fieldName, String.class);
            return this;
        }
        if (fields.containsKey(fieldName))
            return this;
        fields.put(fieldName, cls);
        return this;
    }

    /*
     * @public:     removeField
     * @note:       Remove a field(column) from the table.
     * @param:      [(String) fieldName] The field name.
     * @retval:     [Table] "this" is returned.
     */
    public Table removeField(String fieldName) {
        fields.remove(fieldName);
        return this;
    }

    /*
     * @public:     getRowNum
     * @note:       Get the number of rows in the table.
     * @retval:     [int] The number of rows.
     */
    public int getRowNum() {
        return rows.size();
    }

    /*
     * @public:     addRow
     * @note:       Add a new row to the table.
     * @param:      [(Pair<String,Object>[]) initializers] The initializers. Can be null.
     * @retval:     [Table] "this" is returned.
     */
    public Table addRow(Pair<String, Object>[] initializers) {
        return addRow(getRowNum(), initializers);
    }

    /*
     * @public:     addRow
     * @note:       Add a new row to the table at a specific position.
     * @param:      [(int) rowIndex] The position where the row will be added.
     * @param:      [(Pair<String,Object>[]) initializers] The initializers. Can be null.
     * @retval:     [Table] "this" is returned.
     */
    public Table addRow(int rowIndex, Pair<String, Object>[] initializers) {
        Map<String, Object> row = new HashMap<String, Object>();
        if (initializers != null) {
            for (Pair<String, Object> initializer : initializers) {
                if (!fields.containsKey(initializer.getKey()))
                    continue;
                row.put(initializer.getKey(), initializer.getValue());
            }
        }
        rows.add(rowIndex, row);
        return this;
    }

    /*
     * @public:     removeRow
     * @note:       Remove a row from the table.
     * @param:      [(int) rowIndex] The position where the row will be removed.
     * @retval:     [Table] "this" is returned.
     */
    public Table removeRow(int rowIndex) {
        rows.remove(rowIndex);
        return this;
    }

    /*
     * @public:     setValue
     * @note:       Set a value in the table.
     * @param:      [(int) rowIndex] The row ID.
     * @param:      [(String) fieldName] The field name.
     * @param:      [(Object) value] The value.
     * @retval:     [Table] "this" is returned.
     */
    public Table setValue(int rowIndex, String fieldName, Object value) {
        if (rowIndex < 0 || rowIndex >= rows.size())
            return this;
        if (!fields.containsKey(fieldName))
            return this;
        rows.get(rowIndex).put(fieldName, value);
        return this;
    }

    /*
     * @public:     getValue
     * @note:       Get a value from the table.
     * @param:      [(int) rowIndex] The row ID.
     * @param:      [(String) fieldName] The field name.
     * @retval:     [Object] The value.
     */
    public Object getValue(int rowIndex, String fieldName) {
        if (rowIndex < 0 || rowIndex >= rows.size())
            return null;
        if (!fields.containsKey(fieldName))
            return null;
        return rows.get(rowIndex).get(fieldName);
    }
    public List<Map<String, Object>> getEntries(Predicate<Map<String, Object>> conditions){
        List<Map<String, Object>> targets=new ArrayList<>();
        for (Map<String, Object> entry:
             rows) {
            if(conditions.test(entry)){
                targets.add(entry);
            }
        }
        return targets;
    }
    public Map<String, Object> getEntry(Predicate<Map<String, Object>> conditions){
        Map<String, Object> target = null;
        for (Map<String, Object> entry:
                rows) {
            if(conditions.test(entry)){
                target=entry;
            }
        }
        return target;
    }
    public Map<String, Object> getRowRaw(int rowIndex){
        return rows.get(rowIndex);
    }
    public Map<String, Object> setRowRaw(int rowIndex, Map<String, Object> row){
        return rows.set(rowIndex,row);
    }
    public void addRowRaw(int rowIndex,Map<String, Object> row){
        rows.add(rowIndex,row);
    }
    public void addRowRaw(Map<String, Object> row){
        addRowRaw(rows.size(),row);
    }
    public void forEach(Consumer<Map<String,Object>> mapConsumer){
        for (Map<String,Object> row:
             rows) {
            mapConsumer.accept(row);
        }
    }

    /*
     * @public:     serialize
     * @note:       Serialize the table.
     * @param:      [(int) tableId] The table ID.
     * @retval:     [String] The string-style serialized table.
     */
    public String serialize(int tableId) {
        String prefix = "table." + tableId;
        String result = prefix + ".name:" + tableName + ";";
        for (String key : fields.keySet()) {
            Class<?> value = fields.get(key);
            result += prefix + ".field." + key + ":" + Types.cls2str(value) + ";";
        }
        result += prefix + ".rowNum:" + getRowNum() + ";";
        for (int i = 0; i < getRowNum(); i++) {
            Map<String, Object> row = rows.get(i);
            for (String key : row.keySet()) {
                Object obj = row.get(key);
                if (obj == null)
                    continue;
                Class<?> cls = fields.get(key);
                if (cls == null) {
                    continue;
                }
                result += prefix + ".row." + i + "." + key + ":" + Types.serialize(obj, cls) + ";";
            }
        }
        return result;
    }

    /*
     * @public:     deserialize
     * @note:       Deserialize the table.
     * @param:      [(String) str] The string-style serialized table.
     * @param:      [(int) tableId] The table ID.
     * @param:      [(boolean) readName] Setting to true to overwrite the current table name.
     * @param:      [(boolean) readField] Setting to true to overwrite the current field configuration.
     * @retval:     [Table] "this" is returned.
     */
    public Table deserialize(String str, int tableId, boolean readName, boolean readField) {
        if (readField)
            fields = new HashMap<String, Class<?>>();
        rows = new ArrayList<Map<String, Object>>();
        str = str.replace("\\s", "");
        String[] strSegments = str.split(";");
        String prefix = "table." + tableId;
        for (String strSeg : strSegments) {
            String[] strParts = strSeg.split(":");
            if (strParts.length < 2) continue;
            String strKey = strParts[0];
            String strValue = strParts[1];
            if (readName && strKey.equals(prefix + ".name")) {
                tableName = strValue;
            }
            if (strKey.equals(prefix + ".rowNum")) {
                for (int i = 0; i < Integer.parseInt(strValue); i++) {
                    //Fixed
                    addRow(null);
                }
            }
            if (readField && strKey.startsWith(prefix + ".field.")) {
                String fieldName = strKey.substring(strKey.lastIndexOf(".") + 1);
                Class<?> fieldType = Types.str2cls(strValue);
                if (fieldType == null)
                    continue;
                fields.put(fieldName, fieldType);
            }
        }
        for (String strSeg : strSegments) {
            String[] strParts = strSeg.split(":");
            String strKey = strParts[0];
            String strValue = strParts[1];
            if (strKey.startsWith(prefix + ".row.")) {
                String[] strSubParts = strKey.split("\\.");
                int rowId = Integer.parseInt(strSubParts[strSubParts.length - 2]);
                String fieldName = strSubParts[strSubParts.length - 1];
                Class<?> fieldType = fields.get(fieldName);
                if (fieldType == null)
                    continue;
                setValue(rowId, fieldName, Types.deserialize(strValue, fieldType));
            }
        }
        return this;
    }

    /*
     * @public:     deserialize
     * @note:       Deserialize the table (simpler version).
     * @param:      [(String) str] The string-style serialized table.
     * @retval:     [Table] "this" is returned.
     */
    public Table deserialize(String str, boolean readField) {
        int tableId = getTableId(str);
        if (tableId == -1)
            return this;
        deserialize(str, tableId, false, readField);
        return this;
    }

    /*
     * @public:     getTableId
     * @note:       Retrieve the table ID from a string-style serialized table.
     * @param:      [(String) tableName] The table name.
     * @param:      [(String) str] The string-style serialized table.
     * @retval:     [int] The table ID. Value -1 stands for "not found".
     */
    public int getTableId(String str) {
        str = str.replace("\\s", "");
        String[] strSegments = str.split(";");
        for (String strSeg : strSegments) {
            String[] strParts = strSeg.split(":");
            if (strParts.length < 2) continue;
            String strKey = strParts[0];
            String strValue = strParts[1];
            if (strKey.matches("table\\.[0-9]+\\.name")) {
                if (!strValue.equals(tableName))
                    continue;
                String[] strSubParts = strKey.split("\\.");
                return Integer.parseInt(strSubParts[1]);
            }
        }
        return -1;
    }
}
