package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.util.amazon.AWSClients
import jetbrains.buildServer.util.amazon.AWSCommonParams

class AWSClientsProviderImpl : AWSClientsProvider {
    override fun <T> withClient(bean: SQSBean, func: AWSClients.() -> T): T = AWSCommonParams.withAWSClients<T, Exception>(bean.toMap()) { func.invoke(it) }
}