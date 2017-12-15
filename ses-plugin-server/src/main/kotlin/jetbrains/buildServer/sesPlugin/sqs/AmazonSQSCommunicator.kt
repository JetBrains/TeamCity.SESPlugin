package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface AmazonSQSCommunicator {
    @Throws(AmazonSQSCommunicationException::class)
    fun <T> withCommunication(bean: SQSBean, func: (data: AmazonSQSCommunicationData) -> T): T
}