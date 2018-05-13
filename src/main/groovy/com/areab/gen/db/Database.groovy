package com.areab.gen.db

import groovy.transform.Canonical
import groovy.transform.CompileStatic

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