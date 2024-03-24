# MIEQ
A simple query builder to easily define your database and query through the data.

# How to set up

## IntelliJ IDE

1. Download the latest version of _mieq-vx.x.x.jar_ file available in the Releases page.
2. Move the download .jar file into the following folder:
    ``<your_project>/build/libs/``
    
    - If you don't want to use the suggested path you can create another folder for external libraries. You only have to add that custom folder as a library folder by right-clicking the folder and choosing 'Add as Library' option.
      - <img src="https://github.com/zaytiri/MIEQ/blob/main/readme-images/1.png" width="200" height="400" />
3. Add the following dependency in your project's _build.gradle_ file:
    ```
    dependencies {
        implementation files('build/libs/mieq-vx.x.x.jar')
    }
    ```
4. Reload Gradle.
5. Add ``requires MIEQ;`` in your _module-info.java_ file.

Note: Replace the _vx.x.x_ for the downloaded version.

## Android Studio

Follow the steps in [this link](https://www.geeksforgeeks.org/how-to-import-external-jar-files-in-android-studio/).

# How to use

MIEQ has 2 main components:
- Main statements, which lets you query the data (e.g. Insert, Select, Create, Update, Delete);
- Database Schema, which lets you define the database schema and converts that to code.

## Main statements

### Options

#### Set Close Connection
Each query has the option to set if the connection should be closed after the next statement or not.
Using ``query.setCloseConnection(false)`` allows to have multiple statements without the connection closing after each statement done.
By default this is ``true``, meaning that after each statement the connection will close automatically.

Example:

```java
        SelectQueryBuilder selectAll = new SelectQueryBuilder(connection.open());
        selectAll.select()
                .from("table");

        return selectAll.execute();
```

In this example, the ``closeConnection`` variable is true, meaning that after executing the ``query``, the connection will be close automatically. ``query`` cannot be re-used anymore and a new instance must be created like new ``SelectQueryBuilder selectAFew = SelectQueryBuilder(connection.open())``.

In the Create example, by setting ``closeConnection`` to false, all statements inside the for loop will be done without interruptions. If set to false, the connection must be closed manually.

### Create

```java
    private Connection connection; // Connection is a class from java.sql

    // Opens the connection to the database. In this case we are using JDBC driver
    public Connection open() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

    // Closes the connection to the database.
    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Creates a database using MIEQ Create statement.
    public void createDatabase() {
        CreateTableQueryBuilder query = new CreateTableQueryBuilder(open());
        query.setCloseConnection(false);

        for (Table tb : schema.getTables()) {
            query.create(tb);
            query.execute();
        }

        close();
    }
```

### More examples

More examples can be found in the following github repository: [taskerlyze - Repositories](https://github.com/zaytiri/taskerlyze/blob/main/api/src/main/java/personal/zaytiri/taskerlyze/app/persistence/repositories/base/Repository.java)

## Database Schema

To use any statement, there must be tables available in code. To achieve this it's possible to define the database schema by providing a xml file with pre-defined rules.

The xml file must be put directly inside the resources folder, like:
- resources
    - <database_schema_name>.xml

Example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<database>
    <name>database_name</name>
    <table> <!-- table one -->
        <name>one</name>
        <column>
            <name>id</name>
            <type>integer</type>
            <default>1</default>
            <isprimarykey>true</isprimarykey>
        </column>
        <column>
            <name>name</name>
            <type>text</type>
            <default>null</default>
        </column>
    </table>
    <table> <!-- table two -->
        <name>two</name>
        <column>
            <name>id</name>
            <type>integer</type>
            <default>1</default>
            <isprimarykey>true</isprimarykey>
        </column>
        <column>
            <name>name</name>
            <type>text</type>
            <default>null</default>
        </column>
        <column>
            <name>updated_at</name>
            <type>numeric</type>
            <default>null</default>
        </column>
        <column>
            <name>created_at</name>
            <type>numeric</type>
            <default>null</default>
        </column>
    </table>
</database>
```

### Rules

- database
    - name: the name the database must have.
    - table
        - name: the name the table must have.
        - column
            - name: the name of the column.
            - type: the type of the column. If using JDBC driver, the types can be any f the available types in the following lnk [JDBC Data Types](https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm). This property is used when creating the table and to cast the column's value to this type. 
            - default: the default value of the column. Available default values are depending on the column's type. ``null`` is also a available option.
            - is primary key: ``true`` if this column is a primary key or ``false`` if not. Only one column might have this property, since if the property is not set, the default value is ``false``.

### How to use

Before any statements can be used to query the data, a schema must be available. The schema should be initilized when the program launches. To create the schema, the following code should be used:

```java
    Database database = Schema.getSchema( <database_schema_name> );
```

It returns the Database class containing a list of Table, where each table also contains a list of Column. This class is mainly used to easily create each statement to query the data instead of using hardcoded values inside the statements. This usage can be also found in the [More examples](#more-examples) section.

Each class (Database > Table > Column) has useful methods, such as:
- Database
    - getName
    - getTables
    - getTable ( name )
    - (+ all respective setters)

- Table
    - getName
    - getColumns
    - getColumn ( name )
    - getAllColumnsExcept ( listOfColumnNamesToExclude )
    - (+ all respective setters)

- Column
    - getName
    - getTableName
    - getType
    - getDefaultValue
    - getIsPrimaryKey
    - (+ all respective setters)