package com.example.restclient.controller

import com.example.restclient.service.WeatherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("weathers")
class WeatherController(
    private val weatherService: WeatherService,
) {

    @GetMapping
    suspend fun retrieveCurrentWeatherByZipCode(
        @RequestParam("zipCode") zipCode: String
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(weatherService.retrieveCurrentWeatherByZipCode(zipCode))
    }
}