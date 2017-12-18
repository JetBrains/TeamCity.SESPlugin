package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import jetbrains.buildServer.sesPlugin.sqs.result.CheckConnectionResult

class CheckConnectionTask : AmazonSQSCommunicatorTask<CheckConnectionResult> {
    override fun perform(sqs: AmazonSQS, queue: String): CheckConnectionResult {
        return CheckConnectionResult(true)
    }
}