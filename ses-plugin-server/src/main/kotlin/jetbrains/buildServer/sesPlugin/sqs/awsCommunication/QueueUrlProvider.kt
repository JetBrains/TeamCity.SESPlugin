package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface QueueUrlProvider {
    fun getQueueUrl(amazonSQS: AmazonSQS, bean: SQSBean): String
}