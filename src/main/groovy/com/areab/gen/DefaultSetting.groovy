package com.areab.gen

import groovy.transform.CompileStatic


@CompileStatic
class DefaultSetting {

    static String workspaceDirectory = "./workspace"
    static String databaseConfigFile = "./workspace/databases.json"
    static String tableInfoOutputDirectory = "./workspace/tables"
    static String artifactOutputDirectory = "./.dist"
}