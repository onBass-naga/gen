package com.areab.gen.loader

import com.areab.gen.meta.DatabaseMeta
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

class DatabaseMetaLoader {

    static DatabaseMeta load(String filePath) {
        def file = new File(filePath).text
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.readValue(file, DatabaseMeta.class)
    }
}
