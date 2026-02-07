package com.example.restclient.controller

import com.example.restclient.model.Event
import io.awspring.cloud.sqs.operations.SqsTemplate
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(
    private val kafkaTemplate: KafkaTemplate<String, Event>,
    private val sqsTemplate: SqsTemplate,
    @param:Value("\${sqs.queue.name}") private val sqsQueueName: String
) {

    @GetMapping("/messages")
    fun sendMessage(
        @RequestParam message: String
    ): String {
        val event = Event(
            id = UUID.randomUUID().toString(),
            message = message,
            timestamp = System.currentTimeMillis()
        )

        kafkaTemplate.send("test-topic", event.id, event)

        return "Event sent to Kafka: $message"
    }

    @GetMapping("/sqs-messages")
    fun sendSqsEvent(@RequestParam message: String): String {
        val event = Event(
            id = UUID.randomUUID().toString(),
            message = message,
            timestamp = System.currentTimeMillis()
        )

        sqsTemplate.send { to ->
            to.queue(sqsQueueName)
                .payload(event)
                .messageGroupId(event.id)
        }

        return "Event sent to Sqs: $message"
    }
}