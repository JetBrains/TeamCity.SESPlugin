package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotificationParseResult
import jetbrains.buildServer.sesPlugin.data.SESNotificationData
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SESMessagesReceiver(private val sqsMessagesReceiver: SQSMessagesReceiver<AmazonSQSNotification>,
                          private val sesNotificationParser: SESNotificationParser) : SQSMessagesReceiver<SESNotificationData> {
    override fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<SESNotificationData> {
        val received = sqsMessagesReceiver.receiveMessages(bean)
        if (received.exception != null) return AmazonSQSCommunicationResult(emptyList(), received.exception, received.description)

        val res = ArrayList<AmazonSQSNotificationParseResult<SESNotificationData>>()
        received.messages.forEach {
            if (it.result != null) {
                try {
                    res.add(AmazonSQSNotificationParseResult(sesNotificationParser.parse(it.result.Message)))
                } catch (e: Exception) {
                    res.add(AmazonSQSNotificationParseResult(exception = SQSNotificationParseException(cause = e)))
                }
            } else {
                res.add(AmazonSQSNotificationParseResult(exception = it.exception))
            }
        }
        return AmazonSQSCommunicationResult(res)
    }

}