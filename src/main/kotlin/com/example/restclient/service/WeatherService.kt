package com.example.restclient.service

import com.example.restclient.model.WeatherCondition

interface WeatherService {

    suspend fun retrieveCurrentWeatherByZipCode(zipCode: String): WeatherCondition
}