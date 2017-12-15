package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.auth.AWSCredentials

interface SQSAWSClients {
    val credentials: AWSCredentials
    val region: String
}