package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import jetbrains.buildServer.sesPlugin.teamcity.SESBean
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.util.amazon.AWSCommonParams

class SQSMessagesReceiverImpl(private val sqsNotificationParser: SQSNotificationParser) : SQSMessagesReceiver {

    private fun prepareRequest(): ReceiveMessageRequest {
        with(ReceiveMessageRequest()) {
            maxNumberOfMessages = 10

            return this
        }
    }

    override fun receiveMessages(bean: SESBean): ReceiveMessagesResult {
        val params = bean.toMap()

        return AWSCommonParams.withAWSClients<ReceiveMessagesResult, Exception>(params) {
            val credentials: AWSCredentials = it.credentials ?: return@withAWSClients ReceiveMessagesResult(emptyList(), null, "No credentials provided")

            val sqs = try {
                AmazonSQSClientBuilder.standard()
                        .withRegion(it.region)
                        .withCredentials(AWSCredentialsProviderChain(AWSStaticCredentialsProvider(credentials), DefaultAWSCredentialsProviderChain.getInstance()))
                        .build()
            } catch (ex: Exception) {
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "Cannot open connection to Amazon SQS")
            }

            val queueUrlResult = try {
                sqs.getQueueUrl(GetQueueUrlRequest().withQueueName(params[Constants.QUEUE_NAME_PARAM]).withQueueOwnerAWSAccountId(params[Constants.ACCOUNT_ID_PARAM]))
            } catch (ex: Exception) {
                tryShutdownSilently(sqs)
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "Cannot get queue url with name ${params[Constants.QUEUE_NAME_PARAM]} and owner ${params[Constants.ACCOUNT_ID_PARAM]}")
            }

            val messagesResult = try {
                sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrlResult.queueUrl))
            } catch (ex: Exception) {
                return@withAWSClients ReceiveMessagesResult(emptyList(), ex, "No credentials provided")
            } finally {
                tryShutdownSilently(sqs)
            }

            return@withAWSClients messagesResult.messages.map {
                sqsNotificationParser.parse(it.body)
            }.let {
                ReceiveMessagesResult(it)
            }
        }
    }

    override fun checkConnection(bean: SESBean): CheckConnectionResult {
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

    private fun tryShutdownSilently(sqs: AmazonSQS) {
        try {
            sqs.shutdown()
        } catch (ex: Exception) {
        }
    }
}