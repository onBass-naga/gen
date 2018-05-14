package com.areab.gen.db

import java.sql.Types as JavaSqlTypes

enum DataTypes {
    ARRAY(JavaSqlTypes.ARRAY, "ARRAY", "AnyArray"),
    BIGINT(JavaSqlTypes.BIGINT, "BIGINT", "Long"),
    BINARY(JavaSqlTypes.BINARY, "BINARY", "ByteArray"),
    BIT(JavaSqlTypes.BIT, "BIT", "Boolean"),
    BLOB(JavaSqlTypes.BLOB, "BLOB", "Blob"),
    BOOLEAN(JavaSqlTypes.BOOLEAN, "BOOLEAN", "Boolean"),
    CHAR(JavaSqlTypes.CHAR, "CHAR", "String"),
    CLOB(JavaSqlTypes.CLOB, "CLOB", "Clob"),
    DATALINK(JavaSqlTypes.DATALINK, "DATALINK", "Any"),
    DATE(JavaSqlTypes.DATE, "DATE", "LocalDate"),
    DECIMAL(JavaSqlTypes.DECIMAL, "DECIMAL", "BigDecimal"),
    DISTINCT(JavaSqlTypes.DISTINCT, "DISTINCT", "Any"),
    DOUBLE(JavaSqlTypes.DOUBLE, "DOUBLE", "Double"),
    FLOAT(JavaSqlTypes.FLOAT, "FLOAT", "Float"),
    INTEGER(JavaSqlTypes.INTEGER, "INTEGER", "Int"),
    JAVA_OBJECT(JavaSqlTypes.JAVA_OBJECT, "JAVA_OBJECT", "Any"),
    LONGVARBINARY(JavaSqlTypes.LONGVARBINARY, "LONGVARBINARY", "ByteArray"),
    LONGVARCHAR(JavaSqlTypes.LONGVARCHAR, "LONGVARCHAR", "String"),
    LONGNVARCHAR(JavaSqlTypes.LONGNVARCHAR, "LONGNVARCHAR", "String"),
    NCHAR(JavaSqlTypes.NCHAR, "NCHAR", "String"),
    NCLOB(JavaSqlTypes.NCLOB, "NCLOB", "Any"),
    NULL(JavaSqlTypes.NULL, "NULL", "Any"),
    NUMERIC(JavaSqlTypes.NUMERIC, "NUMERIC", "BigDecimal"),
    NVARCHAR(JavaSqlTypes.NVARCHAR, "NVARCHAR", "String"),
    OTHER(JavaSqlTypes.OTHER, "OTHER", "Any"),
    REAL(JavaSqlTypes.REAL, "REAL", "Float"),
    REF(JavaSqlTypes.REF, "REF", "Ref"),
    ROWID(JavaSqlTypes.ROWID, "ROWID", "Any"),
    REF_CURSOR(JavaSqlTypes.REF_CURSOR, "REF_CURSOR", "Any"),
    SMALLINT(JavaSqlTypes.SMALLINT, "SMALLINT", "Short"),
    STRUCT(JavaSqlTypes.STRUCT, "STRUCT", "Struct"),
    SQLXML(JavaSqlTypes.SQLXML, "SQLXML", "Any"),
    TIME(JavaSqlTypes.TIME, "TIME", "LocalTime"),
    TIME_WITH_TIMEZONE(JavaSqlTypes.TIME_WITH_TIMEZONE, "TIME_WITH_TIMEZONE", "ZonedTime"),
    TIMESTAMP(JavaSqlTypes.TIMESTAMP, "TIMESTAMP", "LocalDateTime"),
    TIMESTAMP_WITH_TIMEZONE(JavaSqlTypes.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP_WITH_TIMEZONE", "ZonedDateTime"),
    TINYINT(JavaSqlTypes.TINYINT, "TINYINT", "Byte"),
    VARBINARY(JavaSqlTypes.VARBINARY, "VARBINARY", "ByteArray"),
    VARCHAR(JavaSqlTypes.VARCHAR, "VARCHAR", "String"),

    UNKNOWN(null, "", "Any")

    private Integer javaSqlType
    private String typeName
    private String groupName

    DataTypes(Integer javaSqlType, String typeName, String groupName) {
        this.javaSqlType = javaSqlType
        this.typeName = typeName
        this.groupName = groupName
    }

    String getTypeName() {
        this.typeName
    }

    static DataTypes of(int code) {
        DataTypes type = values().find { it.javaSqlType.intValue() == code}
        type ? type : UNKNOWN
    }
}


enum ScalaDefaultMapping {
    ARRAY(DataTypes.ARRAY,"Array[Any]", null, "Array[Any]()"),
    BIGINT(DataTypes.BIGINT,"Long", null, "0L"),
    BINARY(DataTypes.BINARY,"ByteArray", null, "Array[Byte]()"),
    BIT(DataTypes.BIT,"Boolean", null, "false"),
    BLOB(DataTypes.BLOB,"Blob","java.sql.Blob", "null"),
    BOOLEAN(DataTypes.BOOLEAN,"Boolean",null, "false"),
    CHAR(DataTypes.CHAR,"String",null, "\"\""),
    CLOB(DataTypes.CLOB,"Clob","java.sql.Clob", "null"),
    DATALINK(DataTypes.DATALINK,"Any",null, "null"),
    DATE(DataTypes.DATE,"LocalDate","java.time.LocalDate", "LocalDate.now"),
    DECIMAL(DataTypes.DECIMAL,"BigDecimal",null, "BigDecimal(\"0\")"),
    DISTINCT(DataTypes.DISTINCT,"Any",null, "null"),
    DOUBLE(DataTypes.DOUBLE,"Double",null, "0.0"),
    FLOAT(DataTypes.FLOAT,"Float",null, "0"),
    INTEGER(DataTypes.INTEGER,"Int",null, "0"),
    JAVA_OBJECT(DataTypes.JAVA_OBJECT, "Any",null, "null"),
    LONGVARBINARY(DataTypes.LONGVARBINARY,"ByteArray",null, "Array[Byte]()"),
    LONGVARCHAR(DataTypes.LONGVARCHAR,"String",null, "\"\""),
    LONGNVARCHAR(DataTypes.LONGNVARCHAR,"String",null, "\"\""),
    NCHAR(DataTypes.NCHAR,"String",null, "\"\""),
    NCLOB(DataTypes.NCLOB,"Any",null, "null"),
    NULL(DataTypes.NULL,"Any",null, "null"),
    NUMERIC(DataTypes.NUMERIC,"BigDecimal",null, "BigDecimal(\"0\")"),
    NVARCHAR(DataTypes.NVARCHAR,"String",null, "\"\""),
    OTHER(DataTypes.OTHER,"Any",null, "null"),
    REAL(DataTypes.REAL,"Float",null, "0"),
    REF(DataTypes.REF,"Ref","java.sql.Ref", "null"),
    ROWID(DataTypes.ROWID,"Any",null, "null"),
    REF_CURSOR(DataTypes.REF_CURSOR, "Any",null, "null"),
    SMALLINT(DataTypes.SMALLINT,"Short",null, "0"),
    STRUCT(DataTypes.STRUCT,"Struct","java.sql.Struct", "null"),
    SQLXML(DataTypes.SQLXML,"Any",null, "null"),
    TIME(DataTypes.TIME,"LocalTime","java.time.LocalTime", "LocalTime.now"),
    TIME_WITH_TIMEZONE(DataTypes.TIME_WITH_TIMEZONE, "Any",null, "null"),
    TIMESTAMP(DataTypes.TIMESTAMP,"LocalDate","java.time.LocalDate", "LocalDate.now"),
    TIMESTAMP_WITH_TIMEZONE(DataTypes.TIMESTAMP_WITH_TIMEZONE, "ZonedDateTime","java.time.ZonedDateTime", "ZonedDateTime.now"),
    TINYINT(DataTypes.TINYINT,"Byte",null, "1"),
    VARBINARY(DataTypes.VARBINARY,"ByteArray",null, "Array[Byte]()"),
    VARCHAR(DataTypes.VARCHAR,"String",null, "\"\""),

    UNKNOWN(DataTypes.UNKNOWN,"Any",null, "null")

    private DataTypes dataType
    private String classDefinition
    private String importDefinition
    private String defaultValue

    ScalaDefaultMapping(DataTypes dataType, String classDefinition, String importDefinition, String defaultValue) {
        this.dataType = dataType
        this.classDefinition = classDefinition
        this.importDefinition = importDefinition
        this.defaultValue = defaultValue
    }

    String getTypeName() {
        this.dataType.typeName
    }
    String getClassDefinition() {
        this.classDefinition
    }
    String getImportDefinition() {
        this.importDefinition
    }
    String getDefaultValue() {
        this.defaultValue
    }
}


