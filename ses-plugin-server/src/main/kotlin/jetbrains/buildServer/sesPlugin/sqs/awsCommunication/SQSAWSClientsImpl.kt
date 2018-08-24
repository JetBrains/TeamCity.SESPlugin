package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.auth.AWSCredentials
import jetbrains.buildServer.util.amazon.AWSClients

class SQSAWSClientsImpl(private val realClients: AWSClients) : SQSAWSClients {
    override val credentials: AWSCredentials?
        get() = realClients.credentials
    override val region: String
        get() = realClients.region
}