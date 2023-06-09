package db;

import javafx.util.Pair;
import util.ConfigUtil;
import util.FileIOUtil;
import util.MySqlUtil;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Table> tableList;

    /*
     * @constructor
     * @note:       Construct a fresh clean Database.
     */
    public Database() {
        tableList = new ArrayList<Table>();
    }

    /*
     * @public:     addTable
     * @note:       Add a Table to the database.
     * @param:      [(Table) table] The table.
     * @retval:     [Database] "this" is returned.
     */
    public Database addTable(Table table) {
        tableList.add(table);
        return this;
    }

    /*
     * @public:     removeTable
     * @note:       Remove a Table from the database.
     * @param:      [(String) tableName] The table's name.
     * @retval:     [Database] "this" is returned.
     */
    public Database removeTable(String tableName) {
        for (int i = 0; i < tableList.size(); i++) {
            if (tableName.equals(tableList.get(i).getTableName())) {
                tableList.remove(i);
                break;
            }
        }
        return this;
    }

    /*
     * @public:     getTable
     * @note:       Get table by name.
     * @param:      [(String) tableName] The table's name.
     * @retval:     [Table] The table.
     */
    public Table getTable(String tableName) {
        for (Table table : tableList) {
            if (tableName.equals(table.getTableName()))
                return table;
        }
        return null;
    }

    /*
     * @public:     serialize
     * @note:       Serialize the Database to a file
     * @param:      [(String) filePath] The file path.
     * @retval:     [Database] "this" is returned.
     */
    public Database serialize(String filePath) {
        try {

            StringBuilder serializedData=new StringBuilder();
            String dbSystem=ConfigUtil.getConfig("dbSystem");
            for (int i = 0; i < tableList.size(); i++) {
                serializedData.append(tableList.get(i).serialize(i));
            }
            switch (dbSystem) {
                case "local":
                    FileIOUtil.writeFile(filePath,serializedData.toString());
                    break;
                case "mySql":
                    MySqlUtil.writeData(serializedData.toString());
                    break;
                default:
                    System.out.println("Error: no database system defined");
            }
            System.out.println("Saved data to "+dbSystem+" database");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /*
     * @public:     deserialize
     * @note:       Deserialize the Database from a file.
     * @param:      [(String) filePath] The file path.
     * @retval:     [Database] "this" is returned.
     */
    public Database deserialize(String filePath) {
        String dbSystem=ConfigUtil.getConfig("dbSystem");
        try {
            String dbString = "";
            switch (dbSystem) {
                case "local":
                    dbString = FileIOUtil.readFile(filePath);
                    break;
                case "mySql":
                    dbString = MySqlUtil.readData();
                    break;
                default:
                    System.out.println("Error: no database system defined");
            }
            for (Table table : tableList) {
                table.deserialize(dbString, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Loaded database from "+dbSystem+" system");
        return this;
    }

    public static void main(String[] args) {
        Database db3 = new Database()
                .addTable(new Table("accounts")
                        .addField("name", String.class)
                        .addField("score", Integer.class)
                ).deserialize("testdb.txt");
        System.out.println((Integer) db3.getTable("accounts").getValue(0, "score"));
        // Create a database.
        Database db = new Database()
                // Add a table, named "accounts".
                .addTable(new Table("accounts")
                        // Add two fields(columns).
                        .addField("name", String.class)
                        .addField("score", Integer.class)
                        // Add three blank rows.
                        .addRow(null).addRow(null).addRow((Pair<String, Object>[]) null)
                        // Set some values into the table.
                        .setValue(0, "name", "Ridge")
                        .setValue(0, "score", 0)
                        .setValue(1, "name", "James")
                        .setValue(1, "score", 100)
                        .setValue(2, "name", "hso")
                );
        // Attempt to get the score of row_id = 2. Since it is not set yet, the value is "null".
        System.out.println((Integer) db.getTable("accounts").getValue(2, "score"));
        // Get the "accounts" table.
        Table tAccounts = db.getTable("accounts");
        // Set a new score to row_id = 0.
        tAccounts.setValue(0, "score", 50);
        // Serialize the database to "testdb.txt".
        db.serialize("testdb.txt");
        // Create a new database, and deserialize its data from "testdb.txt".
        Database db2 = new Database()
                .addTable(new Table("accounts")
                        .addField("name", String.class)
                        .addField("score", Integer.class)
                ).deserialize("testdb.txt");
        // Check if the deserialization is successful.
        System.out.println((Integer) db2.getTable("accounts").getValue(0, "score"));
    }
}
