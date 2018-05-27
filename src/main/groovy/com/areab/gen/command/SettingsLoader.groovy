package com.areab.gen.command

import groovy.transform.Canonical
import org.yaml.snakeyaml.Yaml

class SettingsLoader {

    static Settings load(String filePath) {

        def file = new File(filePath)
        if (!file.exists()) {
            throw new IllegalArgumentException("${filePath} is not found")
        }

        Yaml yaml = new Yaml()
        Map map = yaml.load(file.text)
        new Settings(map)
    }
}

@Canonical
class Settings {
    String conflictResolution
    TableInfoSetting tableInfo
    GenerateSetting generate
}

@Canonical
class TableInfoSetting {
    String databases
    String outputDirectory
}

@Canonical
class GenerateSetting {
    List<String> tableFiles
    List<String> ignoreTables
    String template
    String mapping
    String outputDirectory
    FileNameSetting fileName
}

@Canonical
class FileNameSetting {
    String pattern
    TableNameSetting tableName
}

@Canonical
class TableNameSetting {
    String caseStyle
    String inflector
}
