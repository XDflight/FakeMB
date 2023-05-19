package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @public:     addField
     * @note:       Add a field(column) to the table.
     * @param:      [(String) fieldName] The field name.
     * @param:      [(Class<?>) cls] The class of the data-type of the field.
     * @retval:     [Table] "this" is returned.
     */
    public Table addField(String fieldName, Class<?> cls) {
        if (!Types.isSupported(cls))
            return this;
        if (fields.containsKey(fieldName))
            return this;
        fields.put(fieldName, cls);
        return this;
    }

    /*
     * @public:     removeField
     * @note:       Remove a field(column) from the table.
     * @param:      [(String) fieldName] The field name.
     */
    public void removeField(String fieldName) {
        fields.remove(fieldName);
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
     */
    public void addRow() {
        rows.add(new HashMap<String, Object>());
    }

    /*
     * @public:     addRow
     * @note:       Add a new row to the table at a specific position.
     * @param:      [(int) rowIndex] The position where the row will be added.
     * @retval:     [boolean] True if the operation is successful.
     */
    public boolean addRow(int rowIndex) {
        try {
            rows.add(rowIndex, new HashMap<String, Object>());
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /*
     * @public:     removeRow
     * @note:       Remove a row from the table.
     * @param:      [(int) rowIndex] The position where the row will be removed.
     * @retval:     [boolean] True if the operation is successful.
     */
    public boolean removeRow(int rowIndex) {
        try {
            rows.remove(rowIndex);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /*
     * @public:     setValue
     * @note:       Set a value in the table.
     * @param:      [(int) rowIndex] The row ID.
     * @param:      [(String) fieldName] The field name.
     * @param:      [(Object) value] The value.
     * @retval:     [boolean] True if the operation is successful.
     */
    public boolean setValue(int rowIndex, String fieldName, Object value) {
        if (rowIndex < 0 || rowIndex >= rows.size())
            return false;
        if (!fields.containsKey(fieldName))
            return false;
        rows.get(rowIndex).put(fieldName, value);
        return true;
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
                if (cls == null)
                    continue;
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
     */
    public void deserialize(String str, int tableId, boolean readName, boolean readField) {
        if (readField)
            fields = new HashMap<String, Class<?>>();
        rows = new ArrayList<Map<String, Object>>();
        str = str.replace("\\s", "");
        String[] strSegments = str.split(";");
        String prefix = "table." + tableId;
        for (String strSeg : strSegments) {
            String[] strParts = strSeg.split(":");
            String strKey = strParts[0];
            String strValue = strParts[1];
            if (readName && strKey.equals(prefix + ".name")) {
                tableName = strValue;
            }
            if (strKey.equals(prefix + ".rowNum")) {
                for (int i = 0; i < Integer.parseInt(strValue); i++) {
                    addRow();
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
    }

    /*
     * @public:     getTableId
     * @note:       Retrieve the table ID from a string-style serialized table.
     * @param:      [(String) tableName] The table name.
     * @param:      [(String) str] The string-style serialized table.
     * @retval:     [int] The table ID. Value -1 stands for "not found".
     */
    public static int getTableId(String tableName, String str) {
        str = str.replace("\\s", "");
        String[] strSegments = str.split(";");
        for (String strSeg : strSegments) {
            String[] strParts = strSeg.split(":");
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

    public static void main(String[] args) {
        Table table = new Table("accounts")
                .addField("name", String.class)
                .addField("score", Integer.class);
        table.addRow();
        table.addRow();
        table.setValue(0, "name", "Ridge");
        table.setValue(0, "score", 0);
        table.setValue(1, "name", "James");
        String str = table.serialize(0);
        System.out.println(str);
        Table table2 = new Table(0, str);
        System.out.println(table2.getValue(1, "score"));
        System.out.println(Table.getTableId("accounts", str));
    }
}
