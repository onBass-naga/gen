package com.areab.gen.command

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.Canonical
import groovy.transform.CompileStatic

class DatabaseConfigLoader {
    static List<DBSetting> load(String filePath) {

        def file = new File(filePath)
        if (!file.exists()) {
            throw new IllegalArgumentException("${filePath} is not found")
        }

        ObjectMapper mapper = new ObjectMapper()
        def databaseConfig = mapper.readValue(file.text, DatabaseConfig.class)
        databaseConfig.databaseSettings
    }
}

@Canonical
class DatabaseConfig {
    @JsonProperty("databases")
    List<DBSetting> databaseSettings
}

@CompileStatic
@Canonical
class DBSetting {
    String url
    String user
    String password
    String schema

    String getName() {
        this.url.split("/").last()
    }

    DatabaseType getType() {
        DatabaseType.of(this.url)
    }
}

enum DatabaseType {
    PostgreSQL("jdbc:postgresql:", "org.postgresql.Driver")

    private urlPrefix
    private driver

    DatabaseType(String urlPrefix, String driver) {
        this.urlPrefix = urlPrefix
        this.driver = driver
    }

    String getDriver() { return this.driver }

    static DatabaseType of(String url) {
        values().find { type -> url.contains(type.urlPrefix as String)}
    }
}