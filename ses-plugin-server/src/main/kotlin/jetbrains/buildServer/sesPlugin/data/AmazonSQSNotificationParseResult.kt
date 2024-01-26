

package jetbrains.buildServer.sesPlugin.data

import jetbrains.buildServer.sesPlugin.sqs.SQSNotificationParseException

data class AmazonSQSNotificationParseResult<out T>(
        val result: T? = null,
        val exception: SQSNotificationParseException? = null
)