package jetbrains.buildServer.sesPlugin.sqs

data class AmazonSQSNotificationParseResult(
        val result: AmazonSQSNotification?,
        val exception: SQSNotificationParseException?
)