package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.data.SESNotificationData

interface SQSMessageHandler {
    fun accepts(type: String): Boolean
    fun handle(data: SESNotificationData)
}