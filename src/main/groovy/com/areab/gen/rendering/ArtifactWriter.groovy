package com.areab.gen.rendering

import java.nio.file.Files
import java.nio.file.Path

class ArtifactWriter {

    static void write(Path outputDirectory, String contents, String fileName) {

        if (!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory)
        }

        def file = new File(outputDirectory.toFile(), fileName)
        if (!file.exists()) {
            file.createNewFile()
        }

        file.text = contents
    }
}
