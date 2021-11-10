package com.example.cinemaspringapp.config

import com.xebialabs.restito.server.StubServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class IntegrationConfig {

    @Bean(destroyMethod = "stop")
    fun server(@Value("\${stubServer.port}") port: Int): StubServer = StubServer(port).run()
}
