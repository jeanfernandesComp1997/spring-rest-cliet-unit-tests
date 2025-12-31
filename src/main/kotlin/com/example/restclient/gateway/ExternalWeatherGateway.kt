package com.example.restclient.gateway

import com.example.restclient.model.WeatherConditionGatewayResponse

interface ExternalWeatherGateway {

    suspend fun retrieveWeatherByZipCode(zipCode: String): WeatherConditionGatewayResponse
}