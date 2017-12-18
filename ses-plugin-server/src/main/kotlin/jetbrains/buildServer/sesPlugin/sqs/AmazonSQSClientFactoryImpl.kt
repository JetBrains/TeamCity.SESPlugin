package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.sqs.AmazonSQSClientBuilder

class AmazonSQSClientFactoryImpl : AmazonSQSClientFactory {
    override fun createAmazonSQSClient(clients: SQSAWSClients): AutoCloseableAmazonSQS =
            AutoCloseableAmazonSQSImpl(AmazonSQSClientBuilder.standard()
                    .withRegion(clients.region)
                    .withCredentials(AWSCredentialsProviderChain(AWSStaticCredentialsProvider(clients.credentials), DefaultAWSCredentialsProviderChain.getInstance()))
                    .build())
}