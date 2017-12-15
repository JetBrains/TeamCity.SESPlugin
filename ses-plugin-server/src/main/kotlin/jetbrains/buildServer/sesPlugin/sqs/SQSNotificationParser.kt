package jetbrains.buildServer.sesPlugin.sqs

interface SQSNotificationParser {
    fun parse(data: String): AmazonSQSNotificationParseResult<AmazonSQSNotification>
}