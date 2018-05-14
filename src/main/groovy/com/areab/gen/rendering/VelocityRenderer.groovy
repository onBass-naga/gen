package com.areab.gen.rendering

import com.areab.gen.services.Table
import com.google.common.base.CaseFormat
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.jboss.dna.common.text.Inflector


class VelocityRenderer {

    private static Helper helper = new Helper()

    static String render(String templateFilePath, Table table, Map<String, String> constants) {

        StringWriter writer = new StringWriter()

        try {
            Velocity.init()

            VelocityContext context = new VelocityContext()
            context.put("table", table)
            context.put("_", helper)
            context.put("C", constants)

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
}


class Helper {
    String printIf(Boolean condition, String str) {
        condition ? str : ""
    }

    StringWrapper w(String value) {
        new StringWrapper(value)
    }

    ScriptBuilder script(String script) {
        new ScriptBuilder(script)
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
        while(ite.hasNext()) {
            bind.setVariable(ite.next(), ite.next())
        }
        GroovyShell gs = new GroovyShell(bind)
        gs.evaluate(script) as String
    }
}

class StringWrapper {
    private String value

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
        String converted = Inflector.getInstance().singularize(this.value)
        new StringWrapper(converted)
    }

    /**
     * pluralize.
     * ex) box => boxes
     */
    StringWrapper pluralize() {
        String converted = Inflector.getInstance().pluralize(this.value)
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