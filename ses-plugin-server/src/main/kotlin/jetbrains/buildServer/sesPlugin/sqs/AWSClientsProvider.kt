package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.util.amazon.AWSClients

interface AWSClientsProvider {
    fun <T> withClient(bean: SQSBean, func: AWSClients.() -> T): T
}