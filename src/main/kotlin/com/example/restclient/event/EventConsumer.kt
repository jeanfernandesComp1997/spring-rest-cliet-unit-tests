package com.example.restclient.event

import com.example.restclient.model.Event
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service

@Service
class EventConsumer {

    private val logger = LoggerFactory.getLogger(EventConsumer::class.java)

    @RetryableTopic(
        attempts = "3", // Try original + 2 retries
        backoff = Backoff(delay = 2000, multiplier = 2.0), // 2s, then 4s
        autoCreateTopics = "true",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = ["test-topic"], groupId = "test-group")
    fun consume(event: Event, ack: Acknowledgment) {
        logger.info("Processing event: ${event.id}")

        // Simulate a failure for a specific message to test retries
        if (event.message == "fail") {
            throw RuntimeException("Simulated processing failure!")
        }

        logger.info("Successfully processed: ${event.message}")
        ack.acknowledge()
    }

    @DltHandler
    fun handleDlt(event: Event, ack: Acknowledgment) {
        logger.error("EVENT SENT TO DLT: $event. Logic failed after all retries.")
        // Usually, you ACK the DLT so it doesn't get stuck there either
        ack.acknowledge()
    }
}