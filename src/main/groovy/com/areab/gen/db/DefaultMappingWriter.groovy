package com.areab.gen.db

import com.areab.gen.Constants
import com.areab.gen.services.Database
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import java.nio.file.Files
import java.nio.file.Path

class DefaultMappingWriter {

    static void write(Path outputDirectory) {

        def contents = mapping()
        StringWriter writer = new StringWriter()
        try {
            ObjectMapper mapper = new ObjectMapper()
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            mapper.writeValue(writer, contents)

            if (!Files.exists(outputDirectory)) {
                Files.createDirectories(outputDirectory)
            }

            def file = new File(outputDirectory.toFile(), Constants.MAPPING_FILE_NAME)
            if (!file.exists()) {
                file.createNewFile()
            }
            file.text = writer.toString()
        } finally {
            writer.close()
        }
    }

    static MappingWrapper mapping() {
        def map = ScalaDefaultMapping.values().collect { m ->
            new Mapping(m.typeName, m.className)
        }

        new MappingWrapper(map)
//        ["typeMapping", map]
    }
}

@Canonical
@TupleConstructor
class MappingWrapper {
    List<Mapping> typeMapping
}

@Canonical
@TupleConstructor
class Mapping {
    String columnType
    String className
}