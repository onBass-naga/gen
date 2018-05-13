package com.areab.gen.rendering

import com.areab.gen.Constants
import com.areab.gen.db.ScalaDefaultMapping
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import java.nio.file.Files
import java.nio.file.Path

class ArtifactWriter {

    static void write(Path outputDirectory, String contents, String fileName) {

        StringWriter writer = new StringWriter()
        try {
            if (!Files.exists(outputDirectory)) {
                Files.createDirectories(outputDirectory)
            }

            def file = new File(outputDirectory.toFile(), fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            file.text = writer.toString()
        } finally {
            writer.close()
        }
    }
}
