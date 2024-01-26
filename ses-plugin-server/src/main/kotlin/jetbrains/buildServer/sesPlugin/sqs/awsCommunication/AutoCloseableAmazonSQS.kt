

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS

interface AutoCloseableAmazonSQS : AmazonSQS, AutoCloseable