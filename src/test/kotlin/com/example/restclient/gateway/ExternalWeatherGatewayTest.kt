package com.example.restclient.gateway

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

class ExternalWeatherGatewayTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var externalWeatherGateway: ExternalWeatherGatewayImpl

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val restClient = RestClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build()

        externalWeatherGateway = ExternalWeatherGatewayImpl(restClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should retrieve weather infos with success by zipCode`() = runTest {
        // Given
        val zipCode = "37757000"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""{"condition": "Sunny", "temperature": 25}""")

        mockWebServer.enqueue(mockResponse)

        // when
        val result = externalWeatherGateway.retrieveWeatherByZipCode(zipCode)

        // then
        assertEquals("Sunny", result.condition)
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/weathers?zipCode=$zipCode", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun `should throw error when external gateway return 404 NotFound`() = runTest {
        // Given
        val zipCode = "37757000"
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setHeader("Content-Type", "application/json")
            .setBody("""{"message": "zipCode not found"}""")

        mockWebServer.enqueue(mockResponse)

        // when
        assertThrows<HttpClientErrorException.NotFound> {
            externalWeatherGateway.retrieveWeatherByZipCode(zipCode)
        }

        // then
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/weathers?zipCode=$zipCode", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}