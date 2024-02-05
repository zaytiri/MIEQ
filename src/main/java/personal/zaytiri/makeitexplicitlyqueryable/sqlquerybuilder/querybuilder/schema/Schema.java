package personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Schema {

    private static Database SCHEMA;

    private Schema() {
    }

    /**
     * If schema is NULL, then it will read all properties from the user-created xml file with a given fileName and maps them to Database, Table and Column objects.
     *
     * @param fileName: the name of the xml file to be read.
     * @returns a Database object containing every information about the schema depending on xml created properties.
     */
    public static Database getSchema(String fileName) {
        if (SCHEMA != null) {
            return SCHEMA;
        }

        JSONObject json = null;
        try {
            json = XML.toJSONObject(convertXmlFileToString(fileName));
        } catch (FileNotFoundException e) {
            System.err.println("ERR: Database schema xml file was not found.");
            return null;
        }

        Database db = new Database();
        JSONObject database = json.getJSONObject("database");
        String dbName = database.getString("name");
        db.setName(dbName);

        Table table = new Table();
        List<Table> tables = new ArrayList<>();
        List<Column> columns = new ArrayList<>();

        Object ts = database.get("table");
        if (ts instanceof JSONArray) {
            for (Object t : (JSONArray) ts) {
                populateSchema(table, tables, columns, (JSONObject) t);
                table = new Table();
                columns = new ArrayList<>();
            }
        } else if (ts instanceof JSONObject) {
            populateSchema(table, tables, columns, (JSONObject) ts);
        }

        db.setTables(tables);
        SCHEMA = db;

        return SCHEMA;
    }

    private static String convertXmlFileToString(String fileName) throws FileNotFoundException {
        InputStream xmlFile = null;
        try {
            String path = fileName + ".xml";
            xmlFile = Schema.class.getClassLoader().getResource(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader streamReader = new InputStreamReader(xmlFile, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(streamReader);
        String line;
        StringBuilder xml = new StringBuilder();

        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                return "";
            }
            xml.append(line.trim());
        }

        return xml.toString();
    }

    private static void populateSchema(Table table, List<Table> tables, List<Column> columns, JSONObject t) {
        table.setName(t.getString("name"));

        JSONArray cs = t.getJSONArray("column");

        for (Object c : cs) {
            Column column = new Column();
            column.setName(((JSONObject) c).getString("name"));
            column.setType(((JSONObject) c).getString("type"));

            Object defaultValue = ((JSONObject) c).get("default");
            if (defaultValue instanceof String) {
                column.setDefaultValue(((JSONObject) c).getString("default"));
            } else if (defaultValue instanceof Integer) {
                column.setDefaultValue(((JSONObject) c).getInt("default"));
            }
            if (((JSONObject) c).has("isprimarykey")) {
                column.setIsPrimaryKey(((JSONObject) c).getBoolean("isprimarykey"));
            } else {
                column.setIsPrimaryKey(false);
            }

            column.setTableName(t.getString("name"));
            columns.add(column);
        }
        table.setColumns(columns);
        tables.add(table);
    }
}
