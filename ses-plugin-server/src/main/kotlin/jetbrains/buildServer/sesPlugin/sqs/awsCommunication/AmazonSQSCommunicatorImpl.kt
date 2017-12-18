package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class AmazonSQSCommunicatorImpl(private val awsClientsProvider: AWSClientsProvider,
                                private val amazonSQSClientFactory: AmazonSQSClientFactory,
                                private val queueUrlProvider: QueueUrlProvider) : AmazonSQSCommunicator {

    override fun <T> performTask(bean: SQSBean, communicatorTask: AmazonSQSCommunicatorTask<T>): T {
        if (bean.isDisabled()) {
            throw IllegalStateException("Queue is disabled")
        }

        return awsClientsProvider.withClient(bean) {
            amazonSQSClientFactory.createAmazonSQSClient(this).use { sqs ->
                if (Thread.currentThread().isInterrupted) throw InterruptedException("Execution is interrupted")


                return@withClient communicatorTask.perform(sqs, queueUrlProvider.getQueueUrl(sqs, bean))
            }
        }
    }
}