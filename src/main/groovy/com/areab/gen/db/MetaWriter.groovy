package com.areab.gen.db

import com.areab.gen.services.Database
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import java.nio.file.Files
import java.nio.file.Path

class MetaWriter {

    static void write(Database database, Path outputDirectory) {

        StringWriter writer = new StringWriter()
        try {
            ObjectMapper mapper = new ObjectMapper()
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            mapper.writeValue(writer, database)

            if (!Files.exists(outputDirectory)) {
                Files.createDirectories(outputDirectory)
            }

            def file = new File(outputDirectory.toFile(), "${database.databaseName}.json")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.text = writer.toString()
        } finally {
            writer.close()
        }
    }
}
