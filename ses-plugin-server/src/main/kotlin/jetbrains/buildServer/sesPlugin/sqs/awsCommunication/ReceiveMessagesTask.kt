package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import jetbrains.buildServer.sesPlugin.sqs.SQSNotificationParser
import jetbrains.buildServer.sesPlugin.sqs.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.result.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties

class ReceiveMessagesTask(private val properties: TeamCityProperties,
                          private val sqsNotificationParser: SQSNotificationParser) : AmazonSQSCommunicatorTask<AmazonSQSCommunicationResult<AmazonSQSNotification>> {
    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(10)

    override fun perform(sqs: AmazonSQS, queueUrl: String): AmazonSQSCommunicationResult<AmazonSQSNotification> {
        val messagesResult = try {
            sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrl))
        } catch (ex: Exception) {
            return AmazonSQSCommunicationResult(emptyList(), ex, "No credentials provided")
        }

        if (properties.getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false)) {
            for (i in messagesResult.messages) {
                if (Thread.currentThread().isInterrupted) return AmazonSQSCommunicationResult(emptyList(), null, "Execution is interrupted")

                sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
            }
        }

        if (Thread.currentThread().isInterrupted) return AmazonSQSCommunicationResult(emptyList(), null, "Execution is interrupted")

        return messagesResult.messages.map {
            sqsNotificationParser.parse(it.body)
        }.let {
            AmazonSQSCommunicationResult(it)
        }
    }
}