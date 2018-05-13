package com.areab.gen.services

import com.areab.gen.DefaultSetting
import com.areab.gen.rendering.ArtifactWriter
import com.areab.gen.rendering.DatabaseMetaLoader
import com.areab.gen.rendering.StringWrapper
import com.areab.gen.rendering.VelocityRenderer
import groovy.transform.Canonical
import groovy.transform.TupleConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ArtifactGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ArtifactGenerator.class)

    void execute(ArtifactGeneratorOption option) {

        logger.info("ArtifactGenerator: $option")

        def outputDirectoryPath = createOutputDirectory(option)

        def meta = DatabaseMetaLoader.load(option.tablesFile)
        meta.tables.each { table ->
            def result = VelocityRenderer.render(option.templateFile, table)
            println(result)
            String name = new StringWrapper(table.tableName).c2US().singularize().toString()
            ArtifactWriter.write(outputDirectoryPath, result, "${name}.scala")
        }
    }

    Path createOutputDirectory(ArtifactGeneratorOption option) {

        String outputDirectory = option ? option.outputDirectory
                : DefaultSetting.artifactOutputDirectory

        try {
            Path path = Paths.get(outputDirectory)
            if (option?.outputDirectory && !Files.exists(path)) {
                throw new RuntimeException("Not exists: ${outputDirectory}")
            }

            Files.createDirectories(path)

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }
}

@Canonical
@TupleConstructor
class ArtifactGeneratorOption {
    String outputDirectory
    String tablesFile
    String templateFile
}