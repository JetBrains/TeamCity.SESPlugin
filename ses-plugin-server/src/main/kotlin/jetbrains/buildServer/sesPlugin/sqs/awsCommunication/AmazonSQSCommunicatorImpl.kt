package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class AmazonSQSCommunicatorImpl(private val awsClientsProvider: AWSClientsProvider,
                                private val amazonSQSClientFactory: AmazonSQSClientFactory,
                                private val queueUrlGetter: QueueUrlProvider) : AmazonSQSCommunicator {

    @Throws(AmazonSQSCommunicationException::class)
    override fun <T> withCommunication(bean: SQSBean, func: (data: AmazonSQSCommunicationData) -> T): T {
        return awsClientsProvider.withClient(bean) {
            amazonSQSClientFactory.createAmazonSQSClient(this).use {
                if (Thread.currentThread().isInterrupted) throw InterruptedException("Execution is interrupted")

                func.invoke(AmazonSQSCommunicationData(it, queueUrlGetter.getQueueUrl(it, bean)))
            }
        }
    }
}