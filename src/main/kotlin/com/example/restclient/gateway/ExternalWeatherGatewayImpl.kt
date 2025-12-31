package com.example.restclient.gateway

import com.example.restclient.model.WeatherConditionGatewayResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ExternalWeatherGatewayImpl(
    @param:Qualifier("weatherRestClient") private val restClient: RestClient,
) : ExternalWeatherGateway {

    override suspend fun retrieveWeatherByZipCode(zipCode: String): WeatherConditionGatewayResponse {
        return restClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("weathers")
                    .queryParam("zipCode", zipCode)
                    .build()
            }
            .retrieve()
            .body(WeatherConditionGatewayResponse::class.java)
            ?: throw RuntimeException("Weather response body cannot null!")
    }
}