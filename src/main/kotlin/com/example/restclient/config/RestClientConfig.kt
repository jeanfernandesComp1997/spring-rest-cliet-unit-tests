package com.example.restclient.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(
    private val restClientBuilder: RestClient.Builder
) {

    @Bean("weatherRestClient")
    fun weatherRestClient(): RestClient = restClientBuilder.baseUrl("http://localhost:8383").build()
}