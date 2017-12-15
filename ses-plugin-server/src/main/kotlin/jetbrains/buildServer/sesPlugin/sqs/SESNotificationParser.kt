package jetbrains.buildServer.sesPlugin.sqs

interface SESNotificationParser {
    fun parse(data: String): SESNotificationData
}