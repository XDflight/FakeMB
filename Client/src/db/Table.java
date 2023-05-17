package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String tableName;
    private Map<String, Class<?>> fields;
    private List<Map<String, Object>> rows;

    public Table(String tableName) {
        this.tableName = tableName;
        fields = new HashMap<String, Class<?>>();
        rows = new ArrayList<Map<String, Object>>();
    }

    public boolean addField(String fieldName, Class<?> cls) {
        if (!Types.isSupported(cls))
            return false;
        if (fields.containsKey(fieldName))
            return false;
        fields.put(fieldName, cls);
        return true;
    }

    public int getRowNum() {
        return rows.size();
    }

    public void addRow() {
        rows.add(new HashMap<String, Object>());
    }

    public void addRow(int rowIndex) {
        rows.add(rowIndex, new HashMap<String, Object>());
    }

    public boolean setValue(int rowIndex, String fieldName, Object value) {
        if (rowIndex >= rows.size())
            return false;
        if (!fields.containsKey(fieldName))
            return false;
        rows.get(rowIndex).put(fieldName, value);
        return true;
    }

    public Object getValue(int rowIndex, String fieldName) {
        return rows.get(rowIndex).get(fieldName);
    }

    public String serialize(int tableId) {
        String prefix = "table." + tableId;
        String result = prefix + ".name:" + tableName + ";";
        result += prefix + ".rowNum:" + getRowNum() + ";";
        for (int i = 0; i < getRowNum(); i++) {
            Map<String, Object> row = rows.get(i);
            for (String key : row.keySet()) {
                Object obj = row.get(key);
                if (obj == null)
                    continue;
                Class<?> cls = fields.get(key);
                result += prefix + ".row." + i + "." + key + ":" + Types.serialize(obj, cls) + ";";
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Table table = new Table("accounts");
        table.addField("name", String.class);
        table.addField("score", Integer.class);
        table.addRow();
        table.addRow();
        table.setValue(0, "name", "Ridge");
        table.setValue(0, "score", 0);
        table.setValue(1, "name", "James");
        table.setValue(1, "score", 100);
        System.out.println(table.serialize(0));
    }
}
