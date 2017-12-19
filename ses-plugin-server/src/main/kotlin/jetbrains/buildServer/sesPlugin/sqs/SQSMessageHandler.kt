package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.SESNotificationData

interface SQSMessageHandler {
    fun accepts(type: String): Boolean
    fun handle(data: SESNotificationData)
}