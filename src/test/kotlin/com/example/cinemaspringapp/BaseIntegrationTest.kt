package com.example.cinemaspringapp

import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.mongodb.core.MongoOperations

@SpringBootTest(
    classes = [CinemaApp::class],
    properties = ["application.environment=integration"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BaseIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @AfterEach
    fun cleanup() {
        mongoOperations.collectionNames.forEach { collection ->
            mongoOperations.getCollection(collection).deleteMany(
                Document()
            )
        }
    }

}
