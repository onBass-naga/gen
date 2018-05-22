package com.areab.gen.rendering

import com.areab.gen.services.Database
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

class DatabaseMetaLoader {

    static Database load(String filePath) {

        def file = new File(filePath)
        if (!file.exists()) {
            throw new IllegalArgumentException("${filePath} is not found")
        }

        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.readValue(file.text, Database.class)
    }
}
