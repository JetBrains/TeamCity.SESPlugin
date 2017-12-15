package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import jetbrains.buildServer.serverSide.TeamCityProperties
import jetbrains.buildServer.sesPlugin.sqs.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.result.CheckConnectionResult
import jetbrains.buildServer.sesPlugin.sqs.result.ReceiveMessagesResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SQSMessagesReceiverImpl(private val sqsNotificationParser: SQSNotificationParser,
                              private val amazonSQSCommunicator: AmazonSQSCommunicator) : SQSMessagesReceiver<AmazonSQSNotification>, SQSConnectionChecker {

    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(10)

    override fun receiveMessages(bean: SQSBean): ReceiveMessagesResult<AmazonSQSNotification> {
        if (bean.isDisabled()) {
            return ReceiveMessagesResult(emptyList(), null, "Disabled")
        }

        return try {
            amazonSQSCommunicator.withCommunication(bean) {
                val (sqs, queueUrl) = it

                val messagesResult = try {
                    sqs.receiveMessage(prepareRequest().withQueueUrl(it.queueUrl))
                } catch (ex: Exception) {
                    return@withCommunication ReceiveMessagesResult(emptyList(), ex, "No credentials provided")
                }

                if (TeamCityProperties.getBooleanOrTrue("teamcity.sesIntegration.markMessagesAsUnread")) {
                    for (i in messagesResult.messages) {
                        if (Thread.currentThread().isInterrupted) return@withCommunication ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

                        sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
                    }
                }

                if (Thread.currentThread().isInterrupted) return@withCommunication ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

                return@withCommunication messagesResult.messages.map {
                    sqsNotificationParser.parse(it.body)
                }.let {
                    ReceiveMessagesResult(it)
                }
            }
        } catch (e: Exception) {
            ReceiveMessagesResult(emptyList(), e, "Cannot communicate with Amazon SQS")
        }
    }

    override fun checkConnection(bean: SQSBean): CheckConnectionResult {
        if (bean.isDisabled()) {
            return CheckConnectionResult(false, null, "Disabled")
        }

        return try {
            amazonSQSCommunicator.withCommunication(bean) {
                CheckConnectionResult(true)
            }
        } catch (e: Exception) {
            CheckConnectionResult(false, e, "Cannot communicate with Amazon SQS")
        }
    }
}