package com.example.cinemaspringapp.config

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
internal class SwaggerController {
    @GetMapping("/")
    fun redirect(): String {
        return "redirect:swagger-ui.html"
    }
}