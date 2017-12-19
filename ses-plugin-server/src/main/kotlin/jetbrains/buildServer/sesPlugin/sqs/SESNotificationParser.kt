package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.SESNotificationData

interface SESNotificationParser {
    fun parse(data: String): SESNotificationData
}