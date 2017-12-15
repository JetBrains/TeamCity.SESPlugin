package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class AmazonSQSCommunicatorImpl(private val awsClientsProvider: AWSClientsProvider,
                                private val amazonSQSClientFactory: AmazonSQSClientFactory) : AmazonSQSCommunicator {

    @Throws(AmazonSQSCommunicationException::class)
    override fun <T> withCommunication(bean: SQSBean, func: (data: AmazonSQSCommunicationData) -> T): T {
        return awsClientsProvider.withClient(bean) {
            val sqs = amazonSQSClientFactory.createAmazonSQSClient(this)

            if (Thread.currentThread().isInterrupted) throw InterruptedException("Execution is interrupted")

            try {
                val queueUrlResult = try {
                    sqs.getQueueUrl(GetQueueUrlRequest().withQueueName(bean.queueName).withQueueOwnerAWSAccountId(bean.accountId))
                } catch (ex: Exception) {
                    throw AmazonSQSCommunicationException("Cannot get queue url with name ${bean.queueName} and owner ${bean.accountId}", ex)
                }

                func.invoke(AmazonSQSCommunicationData(sqs, queueUrlResult.queueUrl))
            } finally {
                tryShutdownSilently(sqs)
            }
        }
    }

    private fun tryShutdownSilently(sqs: AmazonSQS) {
        try {
            sqs.shutdown()
        } catch (ex: Exception) {
        }
    }
}