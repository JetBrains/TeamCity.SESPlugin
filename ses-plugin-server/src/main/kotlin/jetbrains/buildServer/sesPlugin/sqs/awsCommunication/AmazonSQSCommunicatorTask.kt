

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS

interface AmazonSQSCommunicatorTask<out T> {
    fun perform(sqs: AmazonSQS, queueUrl: String): T
}