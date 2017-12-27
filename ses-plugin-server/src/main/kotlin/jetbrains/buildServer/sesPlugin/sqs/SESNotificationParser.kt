package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.SESNotification

interface SESNotificationParser {
    fun parse(data: String): SESNotification
}