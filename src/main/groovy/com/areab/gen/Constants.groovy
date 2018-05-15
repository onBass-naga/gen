package com.areab.gen

import groovy.transform.CompileStatic


@CompileStatic
class Constants {

    static final String OPTION_KEY_WORKSPACE_DIRECTORY = "--workspace"
    static final String OUTPUT_DIRECTORY = "--output"
    static final String TABLES_FILE = "--tables"
    static final String TEMPLATE_FILE = "--template"
    static final String CONSTANTS_FILE = "--constants"
    static final String SETTINGS_FILE = "--settings"
    static final String MAPPING_FILE = "--mapping"

    static final String DATABASE_SETTING_FILE_NAME = "databases.json"
    static final String DATABASE_SETTING_FILE_SAMPLE_RESOURCE = "skeleton/${DATABASE_SETTING_FILE_NAME}"
    static final String MAPPING_FILE_NAME = "mapping.json"
    static final String SETTING_FILE_NAME = "settings.yaml"
    static final String SETTING_FILE_SAMPLE_RESOURCE = "skeleton/${SETTING_FILE_NAME}"
    static final String CONSTANTS_FILE_NAME = "constants.json"
    static final String CONSTANTS_FILE_SAMPLE_RESOURCE = "skeleton/${CONSTANTS_FILE_NAME}"
    static final String TEMPLATES_DIRECTORY_NAME = "templates"
    static final String SAMPLE_TEMPLATE_FILE_NAME = "ScalaCaseClassSample.vm"
    static final String SAMPLE_TEMPLATE_FILE_NAME_RESOURCE = "skeleton/templates/${SAMPLE_TEMPLATE_FILE_NAME}"

}