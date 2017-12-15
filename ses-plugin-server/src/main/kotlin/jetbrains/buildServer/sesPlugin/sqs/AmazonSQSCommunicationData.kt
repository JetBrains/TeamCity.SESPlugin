package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS

data class AmazonSQSCommunicationData(
        val amazonSQS: AmazonSQS,
        val queueUrl: String
)