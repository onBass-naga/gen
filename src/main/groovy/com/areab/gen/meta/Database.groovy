package com.areab.gen.meta

import groovy.transform.CompileStatic

@CompileStatic
class Database {
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