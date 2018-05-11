package com.areab.gen

import com.areab.gen.loader.DatabaseConfigLoader
import com.areab.gen.loader.DatabaseMetaLoader
import com.areab.gen.meta.MetaGenerator
import com.areab.gen.meta.MetaWriter
import com.areab.gen.rendering.VelocityRenderer
import groovy.transform.CompileStatic
import jp.sf.amateras.mirage.SqlManager
import jp.sf.amateras.mirage.StringSqlResource
import jp.sf.amateras.mirage.session.Session
import jp.sf.amateras.mirage.session.SessionFactory
import org.jboss.dna.common.text.Inflector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@CompileStatic
class GenApplication {
	private static final Logger logger = LoggerFactory.getLogger(GenApplication.class)

	static void main(String[] args) {
//		ApplicationContext ctx = SpringApplication.run(GenApplication, args)
//		def service = ctx.getBean(HelloService.class)
//		service.hello()

		logger.info(Inflector.getInstance().singularize("actionKey"))
		logger.info(Inflector.getInstance().pluralize("actionKey"))
        logger.info(Inflector.getInstance().singularize("action_key"))
		logger.info(Inflector.getInstance().pluralize("action_key"))

//        def dbList = DatabaseConfigLoader.load("src/test/resources/com/areab/gen/loader/databases.json")
//        dbList.each { db ->
//            logger.info(db.toString())
//            def gen = new MetaGenerator(db)
//            def tables = gen.generate()
//            logger.info(tables.toString())
//            def writer = new MetaWriter("workspace/meta")
//            writer.write(db, tables)
//        }

        def meta = DatabaseMetaLoader.load("workspace/meta/pb_users.meta.json")
        meta.tables.each { table ->
            def result = VelocityRenderer.render("workspace/templates/default/ScalaCaseClass.vm", table)
            println(result)
        }


	}
}
