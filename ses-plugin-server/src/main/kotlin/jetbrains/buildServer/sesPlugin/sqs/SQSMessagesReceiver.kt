package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface SQSMessagesReceiver<out T> {
    fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<T>
}