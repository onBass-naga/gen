package com.areab.gen.db

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.Canonical

class DatabaseConfigLoader {
    static List<DBSetting> load(String filePath) {
        def config = new File(filePath).text
        ObjectMapper mapper = new ObjectMapper()
        def databaseConfig = mapper.readValue(config, DatabaseConfig.class)
        databaseConfig.databaseSettings
    }
}

@Canonical
class DatabaseConfig {
    @JsonProperty("databases")
    List<DBSetting> databaseSettings
}