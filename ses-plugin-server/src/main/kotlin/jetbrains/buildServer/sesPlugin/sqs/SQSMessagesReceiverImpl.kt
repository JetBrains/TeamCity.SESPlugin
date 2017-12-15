package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import jetbrains.buildServer.serverSide.TeamCityProperties
import jetbrains.buildServer.sesPlugin.sqs.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.result.CheckConnectionResult
import jetbrains.buildServer.sesPlugin.sqs.result.ReceiveMessagesResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.util.amazon.AWSCommonParams

class SQSMessagesReceiverImpl(private val sqsNotificationParser: SQSNotificationParser) : SQSMessagesReceiver<AmazonSQSNotification>, SQSConnectionChecker {

    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(10)

    override fun receiveMessages(bean: SQSBean): ReceiveMessagesResult<AmazonSQSNotification> {
        val params = bean.toMap()

        if (isDisabled(params)) {
            return ReceiveMessagesResult(emptyList(), null, "Disabled")
        }

        return AWSCommonParams.withAWSClients<ReceiveMessagesResult<AmazonSQSNotification>, Exception>(params) {
            val credentials: AWSCredentials = it.credentials ?: return@withAWSClients ReceiveMessagesResult(emptyList(), null, "No credentials provided")

            val sqs = try {
                AmazonSQSClientBuilder.standard()
                        .withRegion(it.region)
                        .withCredentials(AWSCredentialsProviderChain(AWSStaticCredentialsProvider(credentials), DefaultAWSCredentialsProviderChain.getInstance()))
                        .build()
            } catch (ex: Exception) {
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "Cannot open connection to Amazon SQS")
            }

            if (Thread.currentThread().isInterrupted) return@withAWSClients ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            val queueUrlResult = try {
                sqs.getQueueUrl(GetQueueUrlRequest().withQueueName(params[Constants.QUEUE_NAME_PARAM]).withQueueOwnerAWSAccountId(params[Constants.ACCOUNT_ID_PARAM]))
            } catch (ex: Exception) {
                tryShutdownSilently(sqs)
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "Cannot get queue url with name ${params[Constants.QUEUE_NAME_PARAM]} and owner ${params[Constants.ACCOUNT_ID_PARAM]}")
            }

            if (Thread.currentThread().isInterrupted) return@withAWSClients ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            val messagesResult = try {
                sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrlResult.queueUrl))
            } catch (ex: Exception) {
                tryShutdownSilently(sqs)
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "No credentials provided")
            }

            try {
                if (TeamCityProperties.getBooleanOrTrue("teamcity.sesIntegration.markMessagesAsUnread")) {
                    for (i in messagesResult.messages) {
                        if (Thread.currentThread().isInterrupted) return@withAWSClients ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

                        sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrlResult.queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
                    }
                }
            } finally {
                tryShutdownSilently(sqs)
            }

            if (Thread.currentThread().isInterrupted) return@withAWSClients ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            return@withAWSClients messagesResult.messages.map {
                sqsNotificationParser.parse(it.body)
            }.let {
                ReceiveMessagesResult(it)
            }
        }
    }

    override fun checkConnection(bean: SQSBean): CheckConnectionResult {
        val params = bean.toMap()

        if (isDisabled(params)) {
            return CheckConnectionResult(false, null, "Disabled")
        }

        return AWSCommonParams.withAWSClients<CheckConnectionResult, Exception>(params) {
            val credentials: AWSCredentials = it.credentials ?: return@withAWSClients CheckConnectionResult(false, null, "no credentials provided")

            val sqs = try {
                AmazonSQSClientBuilder.standard()
                        .withRegion(it.region)
                        .withCredentials(AWSCredentialsProviderChain(AWSStaticCredentialsProvider(credentials), DefaultAWSCredentialsProviderChain.getInstance()))
                        .build()
            } catch (ex: Exception) {
                return@withAWSClients CheckConnectionResult(false, ex, "Cannot open connection to Amazon SQS")
            }

            try {
                sqs.getQueueUrl(GetQueueUrlRequest().withQueueName(params[Constants.QUEUE_NAME_PARAM]).withQueueOwnerAWSAccountId(params[Constants.ACCOUNT_ID_PARAM]))
            } catch (ex: Exception) {
                return@withAWSClients CheckConnectionResult(false, ex, "Cannot get queue url")
            } finally {
                tryShutdownSilently(sqs)
            }

            return@withAWSClients CheckConnectionResult(true)
        }
    }

    private fun isDisabled(params: Map<String, String>) =
            !(params[Constants.ENABLED]?.toBoolean() ?: false)

    private fun tryShutdownSilently(sqs: AmazonSQS) {
        try {
            sqs.shutdown()
        } catch (ex: Exception) {
        }
    }
}