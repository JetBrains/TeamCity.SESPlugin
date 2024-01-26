

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface AmazonSQSCommunicator {
    fun <T> performTask(bean: SQSBean, communicatorTask: AmazonSQSCommunicatorTask<T>): T
}