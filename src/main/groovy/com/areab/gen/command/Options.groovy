package com.areab.gen.command

import com.areab.gen.Constants
import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.nio.file.CopyOption
import java.nio.file.StandardCopyOption

class OptionParser {

    static CommandOption parse(Commands command, List<String> optionArgs) {

        def args = optionArgs.isEmpty() ? defaultOption() : optionArgs

        CommandOption option = args.inject(new CommandOption()) { result, arg ->
            String[] dim = arg.split("=")
            Options type = Options.of(dim[0])
            switch (type) {
                case Options.SETTINGS:
                    result = setSettingFileProperties(result as CommandOption, dim[1], command)
                    break
                default:
                    result[type.fieldName] = type.convertFs(dim[1])
            }

            return result as CommandOption
        }

        return option
    }

    private static defaultOption() {
        ["${Options.SETTINGS.optionKey}=${Constants.DEFAULT_WORKSPACE_PATH}/${Constants.SETTING_FILE_NAME}"]
    }

    private static CommandOption setSettingFileProperties(CommandOption option, String filePath, Commands command) {

        if (command == Commands.INIT) { return option }


        def settings = SettingsLoader.load(filePath)

        if (settings?.conflictResolution) {
            option.conflictResolution = ConflictResolution.of(settings.conflictResolution)
        }

        if (command == Commands.TABLE_INFO) {

            def tableInfo = settings?.tableInfo

            if (tableInfo?.databases) {
                option.databases = tableInfo.databases
            }

            if (tableInfo?.outputDirectory) {
                option.outputDirectory = tableInfo.outputDirectory
            }

        } else if (command == Commands.GENERATE) {

            def generate = settings?.generate

            if (generate?.tableFiles) {
                option.tableFiles = generate.tableFiles
            }

            if (generate?.ignoreTables) {
                option.ignoreTables = generate.ignoreTables
            }

            if (generate?.template) {
                option.template = generate.template
            }

            if (generate?.constants) {
                option.constants = generate.constants
            }

            if (generate?.mapping) {
                option.mapping = generate.mapping
            }

            if (generate?.outputDirectory) {
                option.outputDirectory = generate.outputDirectory
            }

            if (generate?.fileName) {
                option.fileNamePattern = generate.fileName.pattern
                option.caseStyle = CaseStyle.of(generate.fileName.tableName.caseStyle)
                option.inflector = Inflector.of(generate.fileName.tableName.inflector)
            }
        }

        return option
    }
}

enum Options {
    CONFLICT_RESOLUTION("--conflictResolution", "-cr", "conflictResolution", {String val -> ConflictResolution.of(val)}),
    OUTPUT_DIRECTORY("--outputDirectory", "-od", "outputDirectory", {String val -> val}),
    DATABASES("--databases", "-d", "databases", {String val -> val}),
    TABLES_DIRECTORY("--tablesDirectory", "-td", "tablesDirectory", {String val -> val}),
    TABLE_FILES("--tableFiles", "-tf", "tableFiles", {String val -> toList(val)}),
    IGNORE_TABLES("--ignoreTables", "-it", "ignoreTables", {String val -> toList(val)}),
    TEMPLATE("--template", "-t", "template", {String val -> val}),
    CONSTANTS("--constants", "-c", "constants", {String val -> val}),
    MAPPING("--mapping", "-m", "mapping", {String val -> val}),
    SETTINGS("--settings", "-s", "", null)

    private String optionKey
    private String shortKey
    private String fieldName
    private Closure convertFs

    Options(String optionKey, String shortKey, String fieldName, Closure convertFs) {
        this.optionKey = optionKey
        this.shortKey = shortKey
        this.fieldName = fieldName
        this.convertFs = convertFs
    }

    String getOptionKey() {
        return this.optionKey
    }

    String getFieldName() {
        return this.fieldName
    }

    Closure getConvertFs() {
        return this.convertFs
    }

    private static List<String> toList(String src) {
        Arrays.asList(src.split(","))
    }

    static Options of(String key) {
        Options type = values().find { it ->
            it.optionKey == key || it.shortKey == key
        }
        if (!type) { throw new IllegalArgumentException("option undefined: ${key}") }
        type
    }
}

@CompileStatic
@Canonical
class CommandOption {
    ConflictResolution conflictResolution

    String outputDirectory
    String databases

    List<String> tableFiles
    List<String> ignoreTables
    String template
    String constants
    String mapping

    String fileNamePattern
    CaseStyle caseStyle
    Inflector inflector
}

enum ConflictResolution {
    OVERWRITE("Overwrite", StandardCopyOption.REPLACE_EXISTING),
    ERROR("Error", null)

    private String code
    private CopyOption copyOption

    ConflictResolution(String code, CopyOption copyOption) {
        this.code = code
        this.copyOption = copyOption
    }

    CopyOption getCopyOption() {
        this.copyOption
    }

    static ConflictResolution of(String code) {
        ConflictResolution type = values().find { it.code.toLowerCase() == code.toLowerCase() }
        if (!type) { throw new IllegalArgumentException("cannot apply to ConflictResolution: ${code}") }
        type
    }
}

enum CaseStyle {

    UPPER_CAMEL("UpperCamel"),
    LOWER_CAMEL("lowerCamel"),
    UPPER_SNAKE("UPPER_SNAKE"),
    LOWER_SNAKE("lower_snake"),
    UPPER_KEBAB("UPPER-KEBAB"),
    LOWER_KEBAB("lower-kebab")

    private String code

    CaseStyle(String code) {
        this.code = code
    }

    static CaseStyle of(String code) {
        CaseStyle type = values().find { it.code.toLowerCase() == code.toLowerCase() }
        if (!type) { throw new IllegalArgumentException("cannot apply to CaseStyle: ${code}") }
        type
    }
}

enum Inflector {

    SINGULARIZE("singularize"),
    PLURALIZE("pluralize"),
    NONE("none")

    private String code

    Inflector(String code) {
        this.code = code
    }

    static Inflector of(String code) {
        Inflector type = values().find { it.code.toLowerCase() == code.toLowerCase() }
        if (!type) { throw new IllegalArgumentException("cannot apply to Inflector: ${code}") }
        type
    }
}
