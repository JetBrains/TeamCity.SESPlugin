package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS

class AutoCloseableAmazonSQSImpl(private val delegate: AmazonSQS) : AmazonSQS by delegate, AutoCloseableAmazonSQS {
    override fun close() {
        delegate.shutdown()
    }
}