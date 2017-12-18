package jetbrains.buildServer.sesPlugin.sqs.result

data class AmazonSQSCommunicationResult<out T>(
        val messages: List<AmazonSQSNotificationParseResult<T>>,
        val exception: Exception? = null,
        val description: String = "ok"
)