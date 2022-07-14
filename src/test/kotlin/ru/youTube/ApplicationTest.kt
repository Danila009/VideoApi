package ru.youTube

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import ru.youTube.routing.configureMainRouting

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureMainRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}