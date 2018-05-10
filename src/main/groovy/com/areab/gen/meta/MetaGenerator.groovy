package com.areab.gen.meta

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import jp.sf.amateras.mirage.SqlManager
import jp.sf.amateras.mirage.StringSqlResource
import jp.sf.amateras.mirage.session.Session

@CompileStatic
class MetaGenerator {

    private Session session
    private Database database

    MetaGenerator(Database database) {
        this.database = database
        this.session = SessionFactory.create(database)
    }

    List<Table> generate() {
        def tableNames = findTableNames()

        tableNames.collect { tableName ->
            def columns = findColumns(tableName)
            def primaryKeys = findPrimaryKeys(tableName)
            new Table(tableName, columns, primaryKeys)
        }
    }

    List<String> findTableNames() {
        try {
            session.begin()
            SqlManager sqlManager = session.getSqlManager()
            def sql = new  StringSqlResource("""
                | SELECT t.table_name 
                | FROM information_schema.tables t
                | WHERE t.table_schema=/*schema*/'public' 
                | ORDER BY t.table_schema,t.table_name
                """.stripMargin().trim())

            sqlManager.getResultList(String.class, sql, this.database)

        } finally {
            session.release()
        }
    }

    List<Column> findColumns(String tableName) {
        try {
            session.begin()
            SqlManager sqlManager = session.getSqlManager()
            def sql = new  StringSqlResource("""
                | SELECT
                |   c.table_schema
                |   , c.table_name
                |   , c.column_name
                |   , c.ordinal_position
                |   , c.column_default
                |   , c.is_nullable
                |   , c.data_type
                |   , c.udt_name
                | FROM 
                |   information_schema.columns c
                | WHERE 
                |   c.table_schema = /*schema*/'public' 
                |   AND c.table_name = /*tableName*/'person';
                """.stripMargin().trim())

            def params = [schema: this.database.schema, tableName: tableName]
            sqlManager.getResultList(Column.class, sql, params)

        } finally {
            session.release()
        }
    }

    List<PrimaryKey> findPrimaryKeys(String tableName) {
        try {
            session.begin()
            SqlManager sqlManager = session.getSqlManager()
            def sql = new  StringSqlResource("""
                | SELECT 
                |   c.column_name, 
                |   c.ordinal_position
                | FROM 
                |   information_schema.key_column_usage AS c
                |   LEFT JOIN information_schema.table_constraints AS t
                |     ON t.constraint_name = c.constraint_name
                | WHERE 
                |   t.table_schema = /*schema*/'public' 
                |   AND t.table_name = /*tableName*/'persons' 
                |   AND t.constraint_type = 'PRIMARY KEY'
                """.stripMargin().trim())

            def params = [schema: this.database.schema, tableName: tableName]
            sqlManager.getResultList(PrimaryKey.class, sql, params)

        } finally {
            session.release()
        }
    }
}

@Canonical
class DatabaseMeta {
    String databaseName
    List<Table> tables
}

@Canonical
class Table {
    String tableName
    List<Column> columns
    List<PrimaryKey> primaryKeys

    Table(String tableName, List<Column> columns, List<PrimaryKey> primaryKeys) {
        this.tableName = tableName
        this.columns = columns
        this.primaryKeys = primaryKeys
    }
}

@Canonical
class Column {
    String columnName
    Integer ordinalPosition
    String columnDefault
    Boolean isNullable
    String dataType
    String udtName
}

@Canonical
class PrimaryKey {
    String columnName
    Integer ordinalPosition
}