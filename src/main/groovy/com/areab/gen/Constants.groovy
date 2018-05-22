package com.areab.gen

import com.areab.gen.command.ConflictResolution
import groovy.transform.CompileStatic

@CompileStatic
class Constants {

    static final String DEFAULT_WORKSPACE_PATH = "./workspace"
    static final ConflictResolution DEFAULT_CONFLICT_RESOLUTION = ConflictResolution.OVERWRITE
    static final String DEFAULT_TABLE_INFO_OUTPUT_DIRECTORY = "./workspace/tables"
    static final String DEFAULT_ARTIFACT_OUTPUT_DIRECTORY = "./.dist"
    static final String DEFAULT_ARTIFACT_FILENAME_PATTERN = '${tableName}.scala'

    static final String DATABASE_SETTING_FILE_NAME = "databases.json"
    static final String DATABASE_SETTING_FILE_SAMPLE_RESOURCE = "/skeleton/${DATABASE_SETTING_FILE_NAME}"
    static final String MAPPING_FILE_NAME = "mapping.json"
    static final String SETTING_FILE_NAME = "settings.yaml"
    static final String SETTING_FILE_SAMPLE_RESOURCE = "/skeleton/${SETTING_FILE_NAME}"
    static final String CONSTANTS_FILE_NAME = "constants.json"
    static final String CONSTANTS_FILE_SAMPLE_RESOURCE = "/skeleton/${CONSTANTS_FILE_NAME}"
    static final String TEMPLATES_DIRECTORY_NAME = "templates"
    static final String SAMPLE_TEMPLATE_FILE_NAME = "ScalaCaseClassSample.vm"
    static final String SAMPLE_TEMPLATE_FILE_NAME_RESOURCE = "/skeleton/templates/${SAMPLE_TEMPLATE_FILE_NAME}"

}