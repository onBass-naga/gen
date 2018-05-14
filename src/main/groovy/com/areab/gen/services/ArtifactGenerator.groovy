package com.areab.gen.services

import com.areab.gen.DefaultSetting
import com.areab.gen.rendering.ArtifactWriter
import com.areab.gen.rendering.ConstantsLoader
import com.areab.gen.rendering.DatabaseMetaLoader
import com.areab.gen.rendering.FilenameSetting
import com.areab.gen.rendering.SettingsLoader
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
        def constants = ConstantsLoader.load(option.constantsFile)
        def settings = SettingsLoader.load(option.settingsFile)

        meta.tables.each { table ->
            def result = VelocityRenderer.render(option.templateFile, table, constants)
            println(result)
            String filename = name(table, settings.filename)
            ArtifactWriter.write(outputDirectoryPath, result, filename)
        }
    }

    String name(Table table, FilenameSetting setting) {

        String caseStyle = setting.tablename.caseStyle
        StringWrapper org = new StringWrapper(table.tableName)
        StringWrapper styled = caseStyle == "UpperCamel" ? org.c2UC()
                : caseStyle == "lowerCamel" ? org.c2LC()
                : caseStyle == "UPPER_SNAKE" ? org.c2US()
                : caseStyle == "lower_snake" ? org.c2LS()
                : caseStyle == "UPPER-KEBAB" ? org.c2UK()
                : caseStyle == "lower-kebab" ? org.c2LK()
                : org

        String inflector = setting.tablename.inflector
        String tableName = inflector == "singularize" ? styled.singularize().toString()
                : inflector == "pluralize" ? styled.pluralize().toString()
                : styled.toString()

        setting.pattern.replace('${tableName}', tableName)
    }

    Path createOutputDirectory(ArtifactGeneratorOption option) {

        String outputDirectory = option.outputDirectory ? option.outputDirectory
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
    String constantsFile
    String settingsFile
}