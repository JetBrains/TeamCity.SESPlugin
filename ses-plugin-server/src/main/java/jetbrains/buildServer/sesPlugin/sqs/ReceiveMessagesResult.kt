package jetbrains.buildServer.sesPlugin.sqs

data class ReceiveMessagesResult(
        val messages: List<AmazonSQSNotificationParseResult>,
        val exception: Exception? = null,
        val description: String = "ok"
)