package com.example.restclient.config

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class WeatherClientConfigForMockingTest(
    private val restClientBuilder: RestClient.Builder
) {

    fun weatherRestClient(): RestClient = restClientBuilder.baseUrl("http://localhost:8383").build()
}