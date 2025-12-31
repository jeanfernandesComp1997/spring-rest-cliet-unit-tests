package com.example.restclient.service

import com.example.restclient.gateway.ExternalWeatherGateway
import com.example.restclient.model.WeatherCondition
import org.springframework.stereotype.Service

@Service
class WeatherServiceImpl(
    private val weatherGateway: ExternalWeatherGateway
) : WeatherService {


    override suspend fun retrieveCurrentWeatherByZipCode(zipCode: String): WeatherCondition {
        val zipCodeValue: ZipCode = zipCode
        return WeatherCondition
            .fromWeatherConditionGatewayResponse(
                weatherGateway.retrieveWeatherByZipCode(zipCodeValue.normalizeZipCode())
            )
    }
}

typealias ZipCode = String

fun ZipCode.normalizeZipCode(): String = this
    .replace("-", "")
    .replace(" ", "")