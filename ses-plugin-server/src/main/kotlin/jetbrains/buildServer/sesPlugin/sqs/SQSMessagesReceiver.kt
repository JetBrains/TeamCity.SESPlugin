package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.result.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface SQSMessagesReceiver<out T> {
    fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<T>
}