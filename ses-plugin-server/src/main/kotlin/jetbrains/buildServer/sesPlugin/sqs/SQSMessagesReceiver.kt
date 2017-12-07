package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface SQSMessagesReceiver {
    fun receiveMessages(bean: SQSBean): ReceiveMessagesResult
    fun checkConnection(bean: SQSBean): CheckConnectionResult
}