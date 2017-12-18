package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS

interface AutoCloseableAmazonSQS : AmazonSQS, AutoCloseable