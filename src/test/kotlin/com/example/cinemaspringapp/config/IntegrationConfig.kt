package com.example.cinemaspringapp.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

class IntegrationConfig {

    @Bean(destroyMethod = "stop")
    fun server(@Value("\${stubServer.port}") port: Int): WireMockServer =
        WireMockServer(WireMockConfiguration.options().port(port))
            .apply { start()  }
}

class MongoDbTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        private const val MONGO_IMAGE = "mongo:8"
        private val mongo =
            MongoDBContainer(DockerImageName.parse(MONGO_IMAGE)).apply {
                start()
            }
    }

    override fun initialize(ctx: ConfigurableApplicationContext) {
        TestPropertyValues
            .of(
                "spring.data.mongodb.uri=${mongo.replicaSetUrl}",
            ).applyTo(ctx.environment)
    }
}