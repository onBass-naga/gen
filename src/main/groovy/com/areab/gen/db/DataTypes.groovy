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
    ARRAY(DataTypes.ARRAY,"AnyArray"),
    BIGINT(DataTypes.BIGINT,"Long"),
    BINARY(DataTypes.BINARY,"ByteArray"),
    BIT(DataTypes.BIT,"Boolean"),
    BLOB(DataTypes.BLOB,"Blob"),
    BOOLEAN(DataTypes.BOOLEAN,"Boolean"),
    CHAR(DataTypes.CHAR,"String"),
    CLOB(DataTypes.CLOB,"Clob"),
    DATALINK(DataTypes.DATALINK,"Any"),
    DATE(DataTypes.DATE,"LocalDate"),
    DECIMAL(DataTypes.DECIMAL,"BigDecimal"),
    DISTINCT(DataTypes.DISTINCT,"Any"),
    DOUBLE(DataTypes.DOUBLE,"Double"),
    FLOAT(DataTypes.FLOAT,"Float"),
    INTEGER(DataTypes.INTEGER,"Int"),
    JAVA_OBJECT(DataTypes.JAVA_OBJECT, "Any"),
    LONGVARBINARY(DataTypes.LONGVARBINARY,"ByteArray"),
    LONGVARCHAR(DataTypes.LONGVARCHAR,"String"),
    LONGNVARCHAR(DataTypes.LONGNVARCHAR,"String"),
    NCHAR(DataTypes.NCHAR,"String"),
    NCLOB(DataTypes.NCLOB,"Any"),
    NULL(DataTypes.NULL,"Any"),
    NUMERIC(DataTypes.NUMERIC,"BigDecimal"),
    NVARCHAR(DataTypes.NVARCHAR,"String"),
    OTHER(DataTypes.OTHER,"Any"),
    REAL(DataTypes.REAL,"Float"),
    REF(DataTypes.REF,"Ref"),
    ROWID(DataTypes.ROWID,"Any"),
    REF_CURSOR(DataTypes.REF_CURSOR, "Any"),
    SMALLINT(DataTypes.SMALLINT,"Short"),
    STRUCT(DataTypes.STRUCT,"Struct"),
    SQLXML(DataTypes.SQLXML,"Any"),
    TIME(DataTypes.TIME,"LocalTime"),
    TIME_WITH_TIMEZONE(DataTypes.TIME_WITH_TIMEZONE, "ZonedTime"),
    TIMESTAMP(DataTypes.TIMESTAMP,"LocalDateTime"),
    TIMESTAMP_WITH_TIMEZONE(DataTypes.TIMESTAMP_WITH_TIMEZONE, "ZonedDateTime"),
    TINYINT(DataTypes.TINYINT,"Byte"),
    VARBINARY(DataTypes.VARBINARY,"ByteArray"),
    VARCHAR(DataTypes.VARCHAR,"String"),

    UNKNOWN(DataTypes.UNKNOWN,"Any")

    private DataTypes dataType
    private String className

    ScalaDefaultMapping(DataTypes dataType, String className) {
        this.dataType = dataType
        this.className = className
    }

    String getTypeName() {
        this.dataType.typeName
    }
    String getClassName() {
        this.className
    }
}


