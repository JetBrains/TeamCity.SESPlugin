package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

interface AmazonSQSClientFactory {
    fun createAmazonSQSClient(clients: SQSAWSClients): AutoCloseableAmazonSQS
}