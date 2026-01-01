package com.example.restclient.controller

import com.example.restclient.model.Event
import java.util.UUID
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(
    private val kafkaTemplate: KafkaTemplate<String, Event>
) {

    @GetMapping("/send")
    fun sendMessage(
        @RequestParam message: String
    ): String {
        val event = Event(
            id = UUID.randomUUID().toString(),
            message = message,
            timestamp = System.currentTimeMillis()
        )

        // Sending to "test-topic"
        kafkaTemplate.send("test-topic", event.id, event)

        return "Event sent to Kafka: $message"
    }
}