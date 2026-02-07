package com.example.restclient.event

import com.example.restclient.model.Event
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SqsEventConsumer {

    private val logger = LoggerFactory.getLogger(SqsEventConsumer::class.java)

    @SqsListener("\${sqs.queue.name}")
    fun consume(event: Event) {
        logger.info("Received event: $event")
    }
}