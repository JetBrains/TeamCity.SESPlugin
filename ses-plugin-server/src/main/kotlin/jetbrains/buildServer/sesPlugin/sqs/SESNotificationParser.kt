package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.data.SESNotificationData

interface SESNotificationParser {
    fun parse(data: String): SESNotificationData
}