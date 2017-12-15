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

class SQSMessagesReceiverImpl(private val sqsNotificationParser: SQSNotificationParser,
                              private val awsClientsProvider: AWSClientsProvider) : SQSMessagesReceiver<AmazonSQSNotification>, SQSConnectionChecker {

    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(10)

    override fun receiveMessages(bean: SQSBean): ReceiveMessagesResult<AmazonSQSNotification> {
        if (bean.isDisabled()) {
            return ReceiveMessagesResult(emptyList(), null, "Disabled")
        }

        val params = bean.toMap()

        return awsClientsProvider.withClient(bean) {
            val credentials: AWSCredentials = this.credentials ?: return@withClient ReceiveMessagesResult(emptyList(), null, "No credentials provided")

            val sqs = try {
                AmazonSQSClientBuilder.standard()
                        .withRegion(this.region)
                        .withCredentials(AWSCredentialsProviderChain(AWSStaticCredentialsProvider(credentials), DefaultAWSCredentialsProviderChain.getInstance()))
                        .build()
            } catch (ex: Exception) {
                return@withClient ReceiveMessagesResult(emptyList(), ex, "Cannot open connection to Amazon SQS")
            }

            if (Thread.currentThread().isInterrupted) return@withClient ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            val queueUrlResult = try {
                sqs.getQueueUrl(GetQueueUrlRequest().withQueueName(params[Constants.QUEUE_NAME_PARAM]).withQueueOwnerAWSAccountId(params[Constants.ACCOUNT_ID_PARAM]))
            } catch (ex: Exception) {
                tryShutdownSilently(sqs)
                return@withClient ReceiveMessagesResult(emptyList(), ex, "Cannot get queue url with name ${params[Constants.QUEUE_NAME_PARAM]} and owner ${params[Constants.ACCOUNT_ID_PARAM]}")
            }

            if (Thread.currentThread().isInterrupted) return@withClient ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            val messagesResult = try {
                sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrlResult.queueUrl))
            } catch (ex: Exception) {
                tryShutdownSilently(sqs)
                return@withClient ReceiveMessagesResult(emptyList(), ex, "No credentials provided")
            }

            try {
                if (TeamCityProperties.getBooleanOrTrue("teamcity.sesIntegration.markMessagesAsUnread")) {
                    for (i in messagesResult.messages) {
                        if (Thread.currentThread().isInterrupted) return@withClient ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

                        sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrlResult.queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
                    }
                }
            } finally {
                tryShutdownSilently(sqs)
            }

            if (Thread.currentThread().isInterrupted) return@withClient ReceiveMessagesResult(emptyList(), null, "Execution is interrupted")

            return@withClient messagesResult.messages.map {
                sqsNotificationParser.parse(it.body)
            }.let {
                ReceiveMessagesResult(it)
            }
        }
    }

    override fun checkConnection(bean: SQSBean): CheckConnectionResult {
        if (bean.isDisabled()) {
            return CheckConnectionResult(false, null, "Disabled")
        }

        val params = bean.toMap()

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