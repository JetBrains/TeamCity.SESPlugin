package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SESBean

interface SQSMessagesReceiver {
    fun receiveMessages(bean: SESBean): ReceiveMessagesResult
    fun checkConnection(bean: SESBean): CheckConnectionResult
}