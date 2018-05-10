package com.areab.gen.meta

import groovy.transform.CompileStatic
import jp.sf.amateras.mirage.exception.ConfigurationException
import jp.sf.amateras.mirage.session.JDBCSessionImpl
import jp.sf.amateras.mirage.session.Session

import java.lang.reflect.InvocationTargetException

@CompileStatic
class SessionFactory {

    synchronized static Session create(Database database) {
        def props = new Properties()
        props.setProperty("jdbc.driver", database.type.driver)
        props.setProperty("jdbc.url", database.url)
        props.setProperty("jdbc.user", database.user)
        props.setProperty("jdbc.password", database.password)
        props.setProperty("sql.cache", "false")

        try {

            new JDBCSessionImpl(props)

        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Driver class not found.", e)

        } catch (NoSuchMethodException e) {
            throw new ConfigurationException(
                    "sessionClass does not have constructor with argument type of Properties.", e)

        } catch (ClassCastException e) {
            throw new ConfigurationException("sessionClass does not implements Session interface ", e)

        } catch (InstantiationException e) {
            throw new ConfigurationException(e)

        } catch (IllegalAccessException e) {
            throw new ConfigurationException(e)

        } catch (InvocationTargetException e) {
            throw new ConfigurationException(e)

        } catch (SecurityException e) {
            throw new ConfigurationException(e)

        }
    }
}
