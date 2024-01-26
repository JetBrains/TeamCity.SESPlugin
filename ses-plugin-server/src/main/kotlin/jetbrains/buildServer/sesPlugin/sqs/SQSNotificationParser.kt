

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotificationParseResult

interface SQSNotificationParser {
    fun parse(data: String): AmazonSQSNotificationParseResult<AmazonSQSNotification>
}