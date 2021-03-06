package com.areab.gen.services

import com.areab.gen.command.CaseStyle
import com.areab.gen.command.ConflictResolution
import com.areab.gen.command.Inflector
import com.areab.gen.rendering.*
import com.areab.gen.utils.FileUtils
import groovy.transform.Canonical
import groovy.transform.TupleConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path

class ArtifactGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ArtifactGenerator.class)

    void execute(ArtifactGeneratorOption option) {

        logger.debug("ArtifactGenerator: $option")

        def outputDirectoryPath = createOutputDirectory(option)
        def typeMapping = TypeMappingLoader.load(option.mappingFile)

        option.tableFiles.each { it ->
            def meta = DatabaseMetaLoader.load(it)

            def constants = [
                    databaseName: meta.databaseName,
                    schema      : meta.schema
            ]

            def ignores = option.ignoreTables
            meta.tables.findAll { !ignores.contains(it.tableName) }
                    .each { table ->
                def result = VelocityRenderer.render(option.templateFile, table, constants, typeMapping)

                logger.debug(result)

                String filename = name(table, option)
                ArtifactWriter.write(outputDirectoryPath, result, filename)
            }
        }
    }

    String name(Table table, ArtifactGeneratorOption option) {

        StringWrapper org = new StringWrapper(table.tableName)
        CaseStyle caseStyle = option.caseStyle
        StringWrapper styled = caseStyle == CaseStyle.UPPER_CAMEL ? org.c2UC()
                : caseStyle == CaseStyle.LOWER_CAMEL ? org.c2LC()
                : caseStyle == CaseStyle.UPPER_SNAKE ? org.c2US()
                : caseStyle == CaseStyle.LOWER_SNAKE ? org.c2LS()
                : caseStyle == CaseStyle.UPPER_KEBAB ? org.c2UK()
                : caseStyle == CaseStyle.LOWER_KEBAB ? org.c2LK()
                : org

        Inflector inflector = option.inflector
        String tableName = inflector == Inflector.SINGULARIZE ? styled.singularize().toString()
                : Inflector.PLURALIZE ? styled.pluralize().toString()
                : styled.toString()

        option.fileNamePattern.replace('${tableName}', tableName)
    }

    private Path createOutputDirectory(ArtifactGeneratorOption option) {
        FileUtils.createDirectory(option.outputDirectory, option.conflictResolution)
    }
}

@Canonical
@TupleConstructor
class ArtifactGeneratorOption {
    ConflictResolution conflictResolution
    String outputDirectory
    List<String> tableFiles
    List<String> ignoreTables

    String templateFile
    String mappingFile

    String fileNamePattern
    CaseStyle caseStyle
    Inflector inflector
}