package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.SESNotification

interface SQSMessageHandler {
    fun accepts(type: String): Boolean
    fun handle(data: SESNotification)
}