

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.sqs.AmazonSQSClientBuilder

class AmazonSQSClientFactoryImpl : AmazonSQSClientFactory {
    override fun createAmazonSQSClient(clients: SQSAWSClients): AutoCloseableAmazonSQS =
            AutoCloseableAmazonSQSImpl(AmazonSQSClientBuilder.standard()
                    .withRegion(clients.region)
                    .withCredentials(buildCredentials(clients))
                    .build())

    private fun buildCredentials(clients: SQSAWSClients) =
            if (clients.credentials != null) AWSStaticCredentialsProvider(clients.credentials) else DefaultAWSCredentialsProviderChain.getInstance()
}