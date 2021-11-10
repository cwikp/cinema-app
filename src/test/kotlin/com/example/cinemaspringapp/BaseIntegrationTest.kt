package com.example.cinemaspringapp

import com.example.cinemaspringapp.config.IntegrationConfig
import com.xebialabs.restito.server.StubServer
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
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

    @AfterEach
    fun cleanup() {
        mongoOperations.collectionNames.forEach { collection ->
            mongoOperations.getCollection(collection).deleteMany(
                Document()
            )
        }
    }

}
