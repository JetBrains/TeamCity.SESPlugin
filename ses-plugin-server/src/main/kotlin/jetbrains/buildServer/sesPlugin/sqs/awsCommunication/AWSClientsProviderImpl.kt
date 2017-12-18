package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.util.amazon.AWSCommonParams

class AWSClientsProviderImpl : AWSClientsProvider {
    override fun <T> withClient(bean: SQSBean, func: SQSAWSClients.() -> T): T {
        return AWSCommonParams.withAWSClients<T, Exception>(bean.toMap()) { func.invoke(SQSAWSClientsImpl(it)) }
    }
}