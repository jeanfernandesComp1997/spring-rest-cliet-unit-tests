package com.example.restclient.gateway

import com.example.restclient.config.WeatherClientConfigForMockingTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@ExtendWith(MockitoExtension::class)
class ExternalWeatherGatewayTest {

    private lateinit var mockWebServer: MockWebServer

    @Mock
    private lateinit var weatherClientConfigForMockingTest: WeatherClientConfigForMockingTest

    private lateinit var externalWeatherGateway: ExternalWeatherGatewayImpl

    private lateinit var restClient: RestClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val restClient = RestClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build()

        this.restClient = restClient

        externalWeatherGateway = ExternalWeatherGatewayImpl(restClient, weatherClientConfigForMockingTest)
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

    @Test
    fun `should retrieve weather infos with success by zipCode with WeatherClientConfig`() = runTest {
        // Given
        val zipCode = "37757000"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""{"condition": "Sunny", "temperature": 25}""")

        mockWebServer.enqueue(mockResponse)

        `when`(weatherClientConfigForMockingTest.weatherRestClient()).thenReturn(restClient)

        // when
        val result = externalWeatherGateway.retrieveWeatherByZipCodeWithWeatherClientConfig(zipCode)

        // then
        assertEquals("Sunny", result.condition)
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/weathers?zipCode=$zipCode", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}