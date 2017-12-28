package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.SQSNotificationParser
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties

class ReceiveMessagesTask(private val properties: TeamCityProperties,
                          private val sqsNotificationParser: SQSNotificationParser) : AmazonSQSCommunicatorTask<AmazonSQSCommunicationResult<AmazonSQSNotification>> {
    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(10)

    override fun perform(sqs: AmazonSQS, queueUrl: String): AmazonSQSCommunicationResult<AmazonSQSNotification> {
        val messagesResult = try {
            sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrl))
        } catch (ex: Exception) {
            return AmazonSQSCommunicationResult(emptyList(), ex, "Cannot communicate with Amazon SQS")
        }

        if (messagesResult.messages.isEmpty()) {
            return AmazonSQSCommunicationResult(emptyList(), null, "No new messages")
        }

        if (properties.getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false)) {
            for (i in messagesResult.messages) {
                if (Thread.currentThread().isInterrupted) return AmazonSQSCommunicationResult(emptyList(), null, "Execution is interrupted")

                sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
            }
        } else {
            if (properties.getBoolean("teamcity.sesIntegration.deleteReadMessages", true)) {
                val r = DeleteMessageBatchRequest().withQueueUrl(queueUrl)
                messagesResult.messages.forEach {
                    r.entries.add(DeleteMessageBatchRequestEntry(it.messageId, it.receiptHandle))
                }
                sqs.deleteMessageBatch(r)
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