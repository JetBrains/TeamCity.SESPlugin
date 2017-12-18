package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS

class AutoCloseableAmazonSQSImpl(private val delegate: AmazonSQS) : AmazonSQS by delegate, AutoCloseableAmazonSQS {
    override fun close() {
        delegate.shutdown()
    }
}