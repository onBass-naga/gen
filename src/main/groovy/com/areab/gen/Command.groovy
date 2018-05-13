package com.areab.gen

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
        if (args.length == 0) {
            throw new RuntimeException("args length is ZERO")
        }

        String command = args[0]
        Map<String, String> options = new HashMap<>()

        for(String arg: args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] dim = arg.split("=")
                if (dim.length >= 2) {
                    options.put(dim[0], dim[1])
                }
            }
        }

        return Commands.createInstance(command, options)
    }
}

interface Command {
    void run()
}

class InitWorkspaceCommand implements Command {

    private WorkspaceGeneratorOption option

    InitWorkspaceCommand(Map<String, String> options) {
        String value = options.get(Constants.OPTION_KEY_WORKSPACE_DIRECTORY)
        if (value && value.startsWith("~")) {
            throw new RuntimeException("You cannot use a path that start with '~'.")
        } else if (value) {
            option = new WorkspaceGeneratorOption(value)
        }
    }

    void run() {
        new WorkspaceGenerator().execute(option)
    }
}

class ExportTableInfoCommand implements Command {

    private TableInfoFileGeneratorOption option

    ExportTableInfoCommand(Map<String, String> options) {
        String value = options.get(Constants.OUTPUT_DIRECTORY)
        if (value && value.startsWith("~")) {
            throw new RuntimeException("You cannot use a path that start with '~'.")
        } else if (value) {
            option = new TableInfoFileGeneratorOption(value)
        }
    }
    void run() {
        new TableInfoFileGenerator().execute(option)
    }
}

class GenerateCommand implements Command {

    private ArtifactGeneratorOption option

    GenerateCommand(Map<String, String> options) {
        String value = options.get(Constants.OUTPUT_DIRECTORY)
        if (value && value.startsWith("~")) {
            throw new RuntimeException("You cannot use a path that start with '~'.")
        } else if (value) {
            option = new ArtifactGeneratorOption(value)
        }
    }
    void run() {
        new ArtifactGenerator().execute(option)
    }
}


enum Commands {
    INIT("init", "i", InitWorkspaceCommand.class),
    TABLE_INFO("table_info", "t", ExportTableInfoCommand.class),
    GENERATE("generate", "g", GenerateCommand.class)

    private String command
    private String shortCommand
    private Class<Command> clazz

    Commands(String command, String shortCommand, Class<Command> clazz) {
        this.command = command
        this.shortCommand = shortCommand
        this.clazz = clazz
    }

    static Command createInstance(String command, Map<String, String> options) {
        Commands cmd = values().find { it ->
            it.command == command || it.shortCommand == command
        }

        if (cmd) {
            return cmd.clazz.newInstance(options)
        }

        throw new IllegalArgumentException("Command not found: ${command}")
    }
}

