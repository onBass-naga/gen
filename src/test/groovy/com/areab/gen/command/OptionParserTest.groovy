package com.areab.gen.command

import org.junit.Test

class OptionParserTest {

    @Test
    void "tableInfoコマンド実行時にsettings.yamlから値が読み込めること"() {

        Commands command = Commands.TABLE_INFO
        String option = "--settings=src/test/resources/com/areab/gen/command/settings.yaml"
        def actual = OptionParser.parse(command, [option])
        assert actual.outputDirectory == "./workspace"
        assert actual.databases == "./workspace/databases.json"
    }

    @Test
    void "shortNameでもsettings.yamlから値が読み込めること"() {

        Commands command = Commands.TABLE_INFO
        String option = "-s=src/test/resources/com/areab/gen/command/settings.yaml"
        def actual = OptionParser.parse(command, [option])
        assert actual.outputDirectory == "./workspace"
        assert actual.databases == "./workspace/databases.json"
    }

    @Test
    void "generateコマンドでsettings.yamlから値が読み込めること"() {

        Commands command = Commands.GENERATE
        String option = "-s=src/test/resources/com/areab/gen/command/settings.yaml"
        def actual = OptionParser.parse(command, [option])
        assert actual.outputDirectory == "./.dist"
        assert actual.ignoreTables[1] == "some_table"
        assert actual.databases == null
    }

    @Test
    void "複数settingsオプションを指定した時に設定値上書きができること"() {

        Commands command = Commands.GENERATE
        String option1st = "-s=src/test/resources/com/areab/gen/command/settings.yaml"
        String option2nd = "-s=src/test/resources/com/areab/gen/command/overwrite.yaml"
        def actual = OptionParser.parse(command, [option1st, option2nd])

        assert actual.ignoreTables.size() == 1
        assert actual.ignoreTables[0] == "overwrite_table"
        assert actual.template == "./workspace/templates/Dao.vm"
        assert actual.outputDirectory == "./.dist"
    }
}