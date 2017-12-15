package jetbrains.buildServer.sesPlugin.sqs

data class AmazonSQSNotificationParseResult<out T>(
        val result: T? = null,
        val exception: SQSNotificationParseException? = null
)