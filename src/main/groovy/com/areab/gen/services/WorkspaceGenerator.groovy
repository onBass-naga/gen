package com.areab.gen.services

import com.areab.gen.Constants
import com.areab.gen.DefaultSetting
import com.areab.gen.db.DefaultMappingWriter
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@CompileStatic
class WorkspaceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceGenerator.class)

    void execute(WorkspaceGeneratorOption option) {

        logger.info("WorkspaceGenerator: ${option}")

        Path workspace = createWorkspace(option)

        addSettingFiles(workspace)
    }

    void addSettingFiles(Path workspace) {

        try {
            // databases.json
            copyFromResources(Constants.DATABASE_SETTING_FILE_SAMPLE_RESOURCE,
                    new File(workspace.toFile(), Constants.DATABASE_SETTING_FILE_NAME))
            // mapping.json
            DefaultMappingWriter.write(workspace)

            // constants.json
            copyFromResources(Constants.CONSTANTS_FILE_SAMPLE_RESOURCE,
                    new File(workspace.toFile(), Constants.CONSTANTS_FILE_NAME))
            // settings.json
            copyFromResources(Constants.SETTING_FILE_SAMPLE_RESOURCE,
                    new File(workspace.toFile(), Constants.SETTING_FILE_NAME))

            // templates_dir
            File templatesDir = new File(workspace.toString(), Constants.TEMPLATES_DIRECTORY_NAME)
            Files.createDirectories(templatesDir.toPath())
            // sample template
            copyFromResources(Constants.SAMPLE_TEMPLATE_FILE_NAME_RESOURCE,
                    new File(templatesDir, Constants.SAMPLE_TEMPLATE_FILE_NAME))

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }

    void copyFromResources(String from, File dest) {
        InputStream input = null
        try {
            input = this.class.getClassLoader().getResourceAsStream(from)
            Files.copy(input, dest.toPath())
        } finally {
            input?.close()
        }
    }

    Path createWorkspace(WorkspaceGeneratorOption option) {

        String workspaceDirectory = option ? option.workspaceDirectory
                : DefaultSetting.workspaceDirectory

        try {
            Path path = Paths.get(workspaceDirectory)
            if (Files.exists(path)) {
                throw new RuntimeException("Path already exist: ${workspaceDirectory}")
            }

            Files.createDirectories(path)

            return path

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }
}

@Canonical
@TupleConstructor
class WorkspaceGeneratorOption {
    String workspaceDirectory
}