package com.areab.gen

import com.areab.gen.command.Command
import com.areab.gen.command.CommandFactory
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@CompileStatic
class GenApplication {
	private static final Logger logger = LoggerFactory.getLogger(GenApplication.class)

	static void main(String[] args) {

        Command command = CommandFactory.create(args)
        logger.info(command.getClass().name)
        command.run()
	}
}
