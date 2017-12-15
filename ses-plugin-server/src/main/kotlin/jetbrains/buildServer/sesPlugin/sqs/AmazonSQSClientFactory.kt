package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS

interface AmazonSQSClientFactory {
    fun createAmazonSQSClient(clients: SQSAWSClients): AmazonSQS
}