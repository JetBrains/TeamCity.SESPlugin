package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.auth.AWSCredentials
import jetbrains.buildServer.util.amazon.AWSClients

class SQSAWSClientsImpl(private val realClients: AWSClients) : SQSAWSClients {

    init {
        if (realClients.credentials == null) throw IllegalArgumentException("Cannot obtain credentials")
    }

    override val credentials: AWSCredentials
        get() = realClients.credentials!!
    override val region: String
        get() = realClients.region
}