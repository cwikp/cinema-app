package com.example.cinemaspringapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun docket(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.cinemaspringapp"))
            .paths { excludeRoot(it) }
            .build()
            .apiInfo(
                ApiInfoBuilder()
                    .title("Example cinema spring application")
                    .contact(Contact("Pawel Cwik", "https://github.com/cwikp", ""))
                    .build()
            )

    private fun excludeRoot(it: String?) = it != "/"
}

@Controller
internal class SwaggerController {
    @GetMapping("/")
    fun redirect(): String {
        return "redirect:swagger-ui.html"
    }
}