#!/bin/bash

echo "Configuring SQS Queues..."

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test

# 1. Create the Dead Letter Queue (DLQ)
awslocal sqs create-queue \
    --queue-name test-queue-dlq.fifo \
    --attributes FifoQueue=true

# 2. Get the DLQ ARN to link it to the main queue
DLQ_ARN=$(awslocal sqs get-queue-attributes \
    --queue-url http://localhost:4566/000000000000/test-queue-dlq.fifo \
    --attribute-names QueueArn --query 'Attributes.QueueArn' --output text)

# 3. Create the Main FIFO Queue with Redrive Policy (DLQ)
awslocal sqs create-queue \
    --queue-name test-queue.fifo \
    --attributes '{
        "FifoQueue": "true",
        "ContentBasedDeduplication": "true",
        "RedrivePolicy": "{\"deadLetterTargetArn\":\"'"$DLQ_ARN"'\",\"maxReceiveCount\":\"5\"}"
    }'

echo "SQS Queues created successfully!"