package com.example.cinemaspringapp

import com.example.cinemaspringapp.config.IntegrationConfig
import com.xebialabs.restito.server.StubServer
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cache.CacheManager
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [CinemaApp::class, IntegrationConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
class BaseIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @Autowired
    lateinit var stubServer: StubServer

    @Autowired
    lateinit var cacheManager: CacheManager

    @AfterEach
    fun cleanup() {
        clearDatabase()
        invalidateCaches()
    }

    private fun clearDatabase() {
        mongoOperations.collectionNames.forEach { collection ->
            mongoOperations.getCollection(collection).deleteMany(
                Document()
            )
        }
    }

    private fun invalidateCaches() {
        val caches = cacheManager.cacheNames.map { cacheManager.getCache(it) }
        caches.forEach { it?.clear() }
    }
}
