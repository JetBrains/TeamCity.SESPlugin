package jetbrains.buildServer.sesPlugin.sqs

data class ReceiveMessagesResult<out T>(
        val messages: List<AmazonSQSNotificationParseResult<T>>,
        val exception: Exception? = null,
        val description: String = "ok"
)