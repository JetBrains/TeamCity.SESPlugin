package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.SESNotification

class UnknownMessageHandler : SQSMessageHandler {
    override fun accepts(type: String) = true

    override fun handle(data: SESNotification) {
        // todo log
    }
}