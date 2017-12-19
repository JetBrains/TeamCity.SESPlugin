package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.CheckConnectionResult
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.AmazonSQSCommunicator
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.CheckConnectionTask
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class CheckConnectionFacade(private val amazonSQSCommunicator: AmazonSQSCommunicator,
                            private val checkConnectionTask: CheckConnectionTask) : SQSConnectionChecker {
    override fun checkConnection(bean: SQSBean): CheckConnectionResult {
        return amazonSQSCommunicator.performTask(bean, checkConnectionTask)
    }
}