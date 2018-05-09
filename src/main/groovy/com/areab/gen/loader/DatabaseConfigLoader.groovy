package com.areab.gen.loader

import com.areab.gen.meta.Database
import com.fasterxml.jackson.databind.ObjectMapper

class DatabaseConfigLoader {
    static List<Database> load(String filePath) {
        def config = new File(filePath).text
        ObjectMapper mapper = new ObjectMapper()
        def databaseConfig = mapper.readValue(config, DatabaseConfig.class)
        databaseConfig.databases
    }
}

class DatabaseConfig {
    List<Database> databases
}