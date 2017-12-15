package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.result.AmazonSQSNotificationParseResult

interface SQSNotificationParser {
    fun parse(data: String): AmazonSQSNotificationParseResult<AmazonSQSNotification>
}