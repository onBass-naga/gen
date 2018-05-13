package com.areab.gen.loader

import com.areab.gen.db.DatabaseConfigLoader
import org.junit.Test

import static org.junit.Assert.*

class DatabaseConfigLoaderTest {

    @Test
    void jsonファイルを読み込みDatabaseが作成できること() {
        def actual = DatabaseConfigLoader.load("src/test/resources/com/areab/gen/loader/databases.json")
        assertEquals("jdbc:postgresql://localhost:5432/test-db", actual[0].url)
        assertEquals("postgres", actual[0].user)
        assertEquals("xyz", actual[1].password)
    }
}