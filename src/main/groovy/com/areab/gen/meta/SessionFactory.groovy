package com.areab.gen.meta

import groovy.transform.CompileStatic
import jp.sf.amateras.mirage.session.Session
import jp.sf.amateras.mirage.session.SessionFactory as MirageSessionFactory

@CompileStatic
class SessionFactory {

    static Session create(Database database) {
        def props = new Properties()
        props.setProperty("jdbc.driver", database.type.driver)
        props.setProperty("jdbc.url", database.url)
        props.setProperty("jdbc.user", database.user)
        props.setProperty("jdbc.password", database.password)
        MirageSessionFactory.getSession(props)
    }
}
