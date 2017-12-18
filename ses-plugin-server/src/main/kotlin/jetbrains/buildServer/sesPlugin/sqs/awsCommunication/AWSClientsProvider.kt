package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface AWSClientsProvider {
    fun <T> withClient(bean: SQSBean, func: SQSAWSClients.() -> T): T
}