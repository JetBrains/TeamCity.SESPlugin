package jetbrains.buildServer.sesPlugin.sqs

interface AmazonSQSClientFactory {
    fun createAmazonSQSClient(clients: SQSAWSClients): AutoCloseableAmazonSQS
}