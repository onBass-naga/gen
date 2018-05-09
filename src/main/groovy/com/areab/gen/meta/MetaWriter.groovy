package com.areab.gen.meta

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

class MetaWriter {
    private String outputDirectory

    MetaWriter(String outputDirectory) {
        this.outputDirectory = outputDirectory
    }

    void write(Database database, List<Table> tables) {
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        StringWriter writer = new StringWriter()
        def meta = [ databaseName: database.name, tables: tables ]
        mapper.writeValue(writer, meta)

        def dir = new File(outputDirectory)
        if (!dir.exists()) {
            dir.mkdir()
        }

        def file = new File(outputDirectory, "${database.name}.meta.json")
        if (!file.exists()) {
            file.createNewFile()
        }
        file.text = writer.toString()
    }
}
