package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SESSQSMessagesReceiver(private val sqsMessagesReceiver: SQSMessagesReceiver<AmazonSQSNotification>,
                             private val sesNotificationParser: SESNotificationParser) : SQSMessagesReceiver<SESNotificationData> {
    override fun receiveMessages(bean: SQSBean): ReceiveMessagesResult<SESNotificationData> {
        val received = sqsMessagesReceiver.receiveMessages(bean)
        if (received.exception != null) return ReceiveMessagesResult(emptyList(), received.exception, received.description)

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
        return ReceiveMessagesResult(res)
    }

}