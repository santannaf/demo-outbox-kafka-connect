package com.outbox.sample.kafka.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class WebConfig : WebMvcConfigurer {
    @Bean
    fun apiDoc(): OpenAPI? {
        return OpenAPI().info(Info().title("Sample APP").description("Sample APP"))
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}