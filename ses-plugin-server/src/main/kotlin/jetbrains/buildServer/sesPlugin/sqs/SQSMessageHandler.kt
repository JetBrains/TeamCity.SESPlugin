package jetbrains.buildServer.sesPlugin.sqs

interface SQSMessageHandler {
    fun accepts(type: String): Boolean
    fun handle(data: SESNotificationData)
}