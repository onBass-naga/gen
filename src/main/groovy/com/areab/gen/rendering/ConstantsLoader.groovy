package com.areab.gen.rendering

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

class ConstantsLoader {

    static Map<String, String> load(String filePath) {

        if (!filePath) {
            return new HashMap()
        }

        def file = new File(filePath).text
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.readValue(file, HashMap.class)
    }
}
