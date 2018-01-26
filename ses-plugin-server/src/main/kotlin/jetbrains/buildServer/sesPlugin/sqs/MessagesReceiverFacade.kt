package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.AmazonSQSCommunicator
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.ReceiveMessagesTask
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class MessagesReceiverFacade(private val amazonSQSCommunicator: AmazonSQSCommunicator,
                             private val receiveMessagesTask: ReceiveMessagesTask) : SQSMessagesReceiver<AmazonSQSNotification> {
    override fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<AmazonSQSNotification> {
        return try {
            amazonSQSCommunicator.performTask(bean, receiveMessagesTask)
        } catch (ex: Exception) {
            AmazonSQSCommunicationResult(emptyList(), ex, ex.message ?: ex.javaClass.name)
        }
    }
}