package com.areab.gen.services

import com.areab.gen.Constants
import com.areab.gen.command.ConflictResolution
import com.areab.gen.db.DefaultMappingWriter
import com.areab.gen.utils.FileUtils
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@CompileStatic
class WorkspaceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceGenerator.class)

    void execute(WorkspaceGeneratorOption option) {

        logger.debug("WorkspaceGenerator: ${option}")

        Path workspace = createWorkspace(option)

        CopyOption copyOption = option.conflictResolution.copyOption
        addSettingFiles(workspace, copyOption)
    }

    void addSettingFiles(Path workspace, CopyOption copyOption) {

        StandardCopyOption.REPLACE_EXISTING
        try {
            // databases.json
            copyFromResources(Constants.DATABASE_SETTING_FILE_SAMPLE_RESOURCE,
                    new File(workspace.toFile(), Constants.DATABASE_SETTING_FILE_NAME),
                    copyOption)
            // mapping.json
            DefaultMappingWriter.write(workspace)

            // settings.json
            copyFromResources(Constants.SETTING_FILE_SAMPLE_RESOURCE,
                    new File(workspace.toFile(), Constants.SETTING_FILE_NAME),
                    copyOption)

            // templates_dir
            File templatesDir = new File(workspace.toString(), Constants.TEMPLATES_DIRECTORY_NAME)
            Files.createDirectories(templatesDir.toPath())
            // sample template
            copyFromResources(Constants.SAMPLE_TEMPLATE_FILE_NAME_RESOURCE,
                    new File(templatesDir, Constants.SAMPLE_TEMPLATE_FILE_NAME),
                    copyOption)

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }

    void copyFromResources(String from, File dest, CopyOption copyOption) {
        InputStream input = null
        try {
            input = this.class.getClassLoader().getResourceAsStream(from)
            Files.copy(input, dest.toPath(), copyOption)
        } finally {
            input?.close()
        }
    }

    private Path createWorkspace(WorkspaceGeneratorOption option) {
        FileUtils.createDirectory(option.outputDirectory, option.conflictResolution)
    }
}

@Canonical
@TupleConstructor
class WorkspaceGeneratorOption {
    String outputDirectory
    ConflictResolution conflictResolution
}