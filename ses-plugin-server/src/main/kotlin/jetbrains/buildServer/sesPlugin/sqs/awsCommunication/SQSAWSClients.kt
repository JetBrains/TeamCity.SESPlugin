package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.auth.AWSCredentials

interface SQSAWSClients {
    val credentials: AWSCredentials
    val region: String
}