package com.areab.gen.command

import com.areab.gen.Constants
import com.areab.gen.services.ArtifactGenerator
import com.areab.gen.services.ArtifactGeneratorOption
import com.areab.gen.services.TableInfoFileGenerator
import com.areab.gen.services.TableInfoFileGeneratorOption
import com.areab.gen.services.WorkspaceGenerator
import com.areab.gen.services.WorkspaceGeneratorOption
import groovy.transform.CompileStatic


@CompileStatic
class CommandFactory {
    static Command create(String[] args) {

        List<String> inputs = Arrays.asList(args)

        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("command is required")
        }

        Commands command = Commands.of(inputs.head())
        CommandOption option = OptionParser.parse(command, inputs.tail())

        return command.createInstance(option)
    }
}

interface Command {
    void run()
}

class WorkspaceInitializationCommand implements Command {

    private WorkspaceGeneratorOption option

    WorkspaceInitializationCommand(CommandOption cmdOption) {

        String outputDirectory = cmdOption.outputDirectory ? cmdOption.outputDirectory
                : Constants.DEFAULT_WORKSPACE_PATH

        ConflictResolution conflictResolution = cmdOption.conflictResolution ? cmdOption.conflictResolution
                : Constants.DEFAULT_CONFLICT_RESOLUTION

        Validator.checkDirectory(outputDirectory)

        option = new WorkspaceGeneratorOption(outputDirectory, conflictResolution)
    }

    void run() {
        new WorkspaceGenerator().execute(option)
    }
}

class ExportTableInfoCommand implements Command {

    private TableInfoFileGeneratorOption option

    ExportTableInfoCommand(CommandOption cmdOption) {
        String outputDirectory = cmdOption.outputDirectory ? cmdOption.outputDirectory
                : Constants.DEFAULT_TABLE_INFO_OUTPUT_DIRECTORY

        Validator.checkRequired(cmdOption.databases, 'databases')
        List<DBSetting> dbSettings = DatabaseConfigLoader.load(cmdOption.databases)

        option = new TableInfoFileGeneratorOption(outputDirectory, dbSettings)
    }

    void run() {
        new TableInfoFileGenerator().execute(option)
    }
}

class GenerateCommand implements Command {

    private ArtifactGeneratorOption option

    GenerateCommand(CommandOption cmdOption) {

        String outputDirectory = cmdOption.outputDirectory ? cmdOption.outputDirectory
                : Constants.DEFAULT_ARTIFACT_OUTPUT_DIRECTORY

        Validator.checkDirectory(outputDirectory)
        Validator.checkRequired(cmdOption.tableFiles, 'tableFiles')
        Validator.checkRequired(cmdOption.template, 'template')
        Validator.checkRequired(cmdOption.mapping, 'mapping')

        String constants = cmdOption.constants ? cmdOption.constants : ""
        List<String> ignoreTables = cmdOption.ignoreTables ? cmdOption.ignoreTables : []
        String fileNamePattern = cmdOption.fileNamePattern ? cmdOption.fileNamePattern
                : Constants.DEFAULT_ARTIFACT_FILENAME_PATTERN
        CaseStyle caseStyle = cmdOption.caseStyle ? cmdOption.caseStyle : CaseStyle.UPPER_CAMEL
        Inflector inflector = cmdOption.inflector ? cmdOption.inflector : Inflector.SINGULARIZE

        option = new ArtifactGeneratorOption(
                outputDirectory,
                cmdOption.tableFiles,
                ignoreTables,
                cmdOption.template,
                constants,
                cmdOption.mapping,
                fileNamePattern,
                caseStyle,
                inflector
        )
    }

    void run() {
        new ArtifactGenerator().execute(option)
    }
}

class Validator {

    static void checkDirectory(String directoryPath) {
        if (directoryPath.startsWith("~")) {
            throw new IllegalArgumentException("You cannot use a path that starts with '~'. ${directoryPath}")
        }
    }

    static void checkRequired(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("${name} option is required")
        }
    }

    static void checkRequired(List<String> value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("${name} option is required")
        }
    }
}


enum Commands {
    INIT("init", "i", WorkspaceInitializationCommand.class),
    TABLE_INFO("tableInfo", "t", ExportTableInfoCommand.class),
    GENERATE("generate", "g", GenerateCommand.class)

    private String command
    private String shortCommand
    private Class<Command> clazz

    Commands(String command, String shortCommand, Class<Command> clazz) {
        this.command = command
        this.shortCommand = shortCommand
        this.clazz = clazz
    }

    Command createInstance(CommandOption option) {
        return clazz.newInstance(option)
    }

    static Commands of(String command) {
        Commands type = values().find { it.command == command }
        if (!type) {
            throw new IllegalArgumentException("Command not found: ${command}")
        }

        return type
    }
}

