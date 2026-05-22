package com.example.cinemaspringapp

import com.example.cinemaspringapp.config.IntegrationConfig
import com.example.cinemaspringapp.config.MongoDbTestContainerInitializer
import com.github.tomakehurst.wiremock.WireMockServer
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cache.CacheManager
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(
    classes = [CinemaApp::class, IntegrationConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [MongoDbTestContainerInitializer::class])
@ActiveProfiles("integration")
class BaseIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @Autowired
    lateinit var stubServer: WireMockServer

    @Autowired
    lateinit var cacheManager: CacheManager

    @AfterEach
    fun cleanup() {
        clearDatabase()
        invalidateCaches()
        resetStubs()
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

    private fun resetStubs() {
        stubServer.resetAll()
    }
}
