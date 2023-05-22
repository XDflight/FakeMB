package db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private File file;
    private List<Table> tableList;

    public Database(String filePath) {
        this.file = new File(filePath);
        tableList = new ArrayList<Table>();
    }

    public Database addTable(Table table) {
        tableList.add(table);
        return this;
    }

    public Database removeTable(String tableName) {
        for (int i = 0; i < tableList.size(); i++) {
            if (tableName.equals(tableList.get(i).getTableName())) {
                tableList.remove(i);
                break;
            }
        }
        return this;
    }

    public Database serialize() {
        try {
            FileOutputStream fop = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Database deserialize() {

    }

    public static void main(String[] args) {

    }
}
