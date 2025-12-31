package com.example.restclient.model

data class WeatherCondition(
    val temperature: String,
    val condition: String,
) {

    companion object {

        fun fromWeatherConditionGatewayResponse(weatherResponse: WeatherConditionGatewayResponse) =
            WeatherCondition(
                temperature = "${weatherResponse.temperature} ºC",
                condition = weatherResponse.condition,
            )
    }
}
