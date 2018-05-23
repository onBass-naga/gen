package com.areab.gen.rendering

import com.areab.gen.db.MappingWrapper
import com.areab.gen.db.TypeMapping
import com.areab.gen.services.Column
import com.areab.gen.services.Table
import com.google.common.base.CaseFormat
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.modeshape.common.text.Inflector


class VelocityRenderer {

    static String render(String templateFilePath, Table table, Map<String, String> constants,
                         MappingWrapper mapping) {

        StringWriter writer = new StringWriter()

        try {
            Velocity.init()
            def mapper = new TypeMapper(mapping)
            constants.put("importDefinitions", importDefinitions(table.columns, mapper))

            VelocityContext context = new VelocityContext()
            context.put("table", table)
            context.put("_", new Helper())
            context.put("C", constants)
            context.put("M", mapper)

            println("get: " + constants.get("Package"))

            Template template = Velocity.getTemplate(
                    templateFilePath, "UTF-8")
            template.merge(context, writer)
            return writer.toString()

        } catch (Exception e) {
            throw new RuntimeException(e)
        } finally {
            writer.close()
        }
    }

    static String importDefinitions(List<Column> columns, TypeMapper mapper) {
        columns.collect { it ->
                    mapper.importDefinition(it.dataType)
                }
                .findAll { it -> it != null }
                .collect {it -> "import $it"}
                .unique()
                .sort()
                .join("\n")
    }
}


class Helper {

    String printIf(Boolean condition, String str) {
        condition ? str : ""
    }

    StringWrapper W(String value) {
        new StringWrapper(value)
    }

    ScriptBuilder script(String script) {
        new ScriptBuilder(script)
    }
}

class TypeMapper {

    private Map<String, TypeMapping> map

    TypeMapper(MappingWrapper wrapper) {
        map = new HashMap<>()
        wrapper.typeMapping.each { it ->
            map.put(it.columnType, it)
        }
    }

    String classDefinition(String columnType) {
        map.get(columnType)?.classDefinition
    }

    String importDefinition(String columnType) {
        map.get(columnType)?.importDefinition
    }

    String defaultValue(String columnType) {
        map.get(columnType)?.defaultValue
    }
}

class ScriptBuilder {

    private String script
    private List<String> args = []

    ScriptBuilder(String script) {
        this.script = script
    }

    ScriptBuilder(String script, List<String> args) {
        this.script = script
        this.args = args
    }

    ScriptBuilder argument(String variableName, String value) {
        this.args.addAll([variableName, value])
        new ScriptBuilder(this.script, this.args)
    }

    String apply() {
        Binding bind = new Binding()
        Iterator ite = args.iterator()
        while (ite.hasNext()) {
            bind.setVariable(ite.next(), ite.next())
        }
        GroovyShell gs = new GroovyShell(bind)
        gs.evaluate(script) as String
    }
}

class StringWrapper {
    private String value
    private Inflector inflector = new Inflector()

    StringWrapper(String value) {
        this.value = value
    }

    StringWrapper c2L() {
        new StringWrapper(this.value.toString().toLowerCase())
    }

    StringWrapper c2U() {
        new StringWrapper(this.value.toString().toUpperCase())
    }

    /**
     * convert to lowerCamelCase
     */
    StringWrapper c2LC() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.LOWER_CAMEL, src)
        new StringWrapper(converted)
    }

    /**
     * convert to UpperCamelCase
     */
    StringWrapper c2UC() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.UPPER_CAMEL, src)
        new StringWrapper(converted)
    }

    /**
     * convert to UPPER_SNAKE_CASE
     */
    StringWrapper c2US() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.UPPER_UNDERSCORE, src)
        new StringWrapper(converted)
    }

    /**
     * convert to lower_snake_case
     */
    StringWrapper c2LS() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.LOWER_UNDERSCORE, src)
        new StringWrapper(converted)
    }

    /**
     * convert to UPPER-KEBAB-CASE
     */
    StringWrapper c2UK() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.LOWER_HYPHEN, src)
        new StringWrapper(converted.toUpperCase())
    }

    /**
     * convert to lower-kebab-case
     */
    StringWrapper c2LK() {
        String src = this.value.toString()
        String converted = caseFormat(src).to(CaseFormat.LOWER_HYPHEN, src)
        new StringWrapper(converted)
    }

    /**
     * singularize.
     * ex) boxes => box
     */
    StringWrapper singularize() {
        String converted = inflector.singularize(this.value)
        new StringWrapper(converted)
    }

    /**
     * pluralize.
     * ex) box => boxes
     */
    StringWrapper pluralize() {
        String converted = inflector.pluralize(this.value)
        new StringWrapper(converted)
    }

    private CaseFormat caseFormat(String value) {
        boolean isUpperCase = value.chars[0].isUpperCase()

        if (isUpperCase) {
            return (value.contains("_")) ? CaseFormat.UPPER_UNDERSCORE
                    : (value.contains("-")) ? CaseFormat.LOWER_HYPHEN
                    : CaseFormat.UPPER_CAMEL
        } else {
            return (value.contains("_")) ? CaseFormat.LOWER_UNDERSCORE
                    : (value.contains("-")) ? CaseFormat.LOWER_HYPHEN
                    : CaseFormat.LOWER_CAMEL
        }
    }

    @Override
    String toString() {
        this.value
    }
}