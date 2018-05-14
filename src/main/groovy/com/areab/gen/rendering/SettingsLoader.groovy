package com.areab.gen.rendering

import groovy.transform.Canonical
import org.yaml.snakeyaml.Yaml

class SettingsLoader {

    static Settings load(String filePath) {

        String path = filePath ? filePath : "./workspace/settings.yaml"
//        if (!filePath) {
//            return null
//        }

        def fileContents = new File(path).text

        Yaml yaml = new Yaml()
        def map = yaml.load(fileContents)
        new Settings(map)
    }

    static void main(String[] args) {
        load("./workspace/settings.yaml")
    }
}

@Canonical
class Settings {
    String outputDirectory
    String tablesDirectory
    FilenameSetting filename
    List<String> ignoreTables
}

@Canonical
class FilenameSetting {
    String pattern
    TableNameSetting tablename
}

@Canonical
class TableNameSetting {
    String caseStyle
    String inflector
}