package com.areab.gen.services

import com.areab.gen.Constants
import com.areab.gen.command.ConflictResolution
import com.areab.gen.command.DBSetting
import com.areab.gen.db.DataTypes
import com.areab.gen.db.MetaWriter
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import java.sql.ResultSet

@CompileStatic
class TableInfoFileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TableInfoFileGenerator.class)

    void execute(TableInfoFileGeneratorOption option) {

        logger.debug("TableInfoFileGenerator: ${option}")

        def outputDirectoryPath = createOutputDirectory(option)

        option.databaseSettings.each { setting ->
            logger.info(setting.toString())
            def database = generateTableInfoFile(setting)
            MetaWriter.write(database, outputDirectoryPath)
        }
    }

    Database generateTableInfoFile(DBSetting database) {

        Connection connection
        try {
            Class.forName(database.type.driver)
            connection = DriverManager.getConnection(
                    database.url,
                    database.user,
                    database.password
            )

            List<String> tableNames = getTableNames(connection, database)
            List<Table> tables = tableNames.collect { tableName ->
                List<Column> columns = getColumns(connection, database, tableName)
                List<String> primaryKeys = getPrimaryKeys(connection, database, tableName)
                new Table(tableName, columns, primaryKeys)
            }

            new Database(database.name, database.schema, tables)

        } finally {
            connection?.close()
        }
    }

    Path createOutputDirectory(TableInfoFileGeneratorOption option) {

        String outputDirectory = option ? option.outputDirectory
                : Constants.DEFAULT_TABLE_INFO_OUTPUT_DIRECTORY

        try {
            Path path = Paths.get(outputDirectory)
            Files.createDirectories(path)

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }

    List<String> getTableNames(Connection connection, DBSetting database) {
        DatabaseMetaData meta = connection.getMetaData()
        String catalog = null
        ResultSet result = meta.getTables(catalog, database.schema, "%", ["TABLE"] as String[])

        def tableNames = []
        while (result.next()) {
            tableNames.add(result.getString("TABLE_NAME"))
        }
        return tableNames
    }

    List<Column> getColumns(Connection connection, DBSetting database, String tableName) {

        DatabaseMetaData meta = connection.getMetaData()
        String catalog = null
        ResultSet result = meta.getColumns(catalog, database.schema, tableName, "%")

        List<Column> columns = []
        while (result.next()) {
            def name = result.getString("COLUMN_NAME")
            def dataType = result.getInt("DATA_TYPE")
            def isNullable = ["YES", "Y"].contains(result.getString("IS_NULLABLE"))

            columns.add(new Column(name, DataTypes.of(dataType).typeName, isNullable))
        }
        return columns
    }

    List<String> getPrimaryKeys(Connection connection, DBSetting database, String tableName) {

        DatabaseMetaData meta = connection.getMetaData()
        String catalog = null
        ResultSet result = meta.getPrimaryKeys(catalog, database.schema, tableName)

        List<String> keys = []
        while (result.next()) {
            def name = result.getString("COLUMN_NAME")
            keys.add(name)
        }
        return keys
    }
}

@Canonical
@TupleConstructor
class TableInfoFileGeneratorOption {
    String outputDirectory
    List<DBSetting> databaseSettings
    ConflictResolution conflictResolution
}

@Canonical
@TupleConstructor
class Database {
    String databaseName
    String schema
    List<Table> tables
}

@Canonical
@TupleConstructor
class Table {
    String tableName
    List<Column> columns
    List<String> primaryKeys
}

@Canonical
@TupleConstructor
class Column {
    String columnName
    String dataType
    Boolean isNullable
}

