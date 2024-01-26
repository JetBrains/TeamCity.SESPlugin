

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class QueueUrlProviderImpl : QueueUrlProvider {
    override fun getQueueUrl(amazonSQS: AmazonSQS, bean: SQSBean): String {
        val queueName = bean.queueName
        val accountId = bean.accountId

        val queueUrlResult = try {
            amazonSQS.getQueueUrl(GetQueueUrlRequest().withQueueName(queueName).withQueueOwnerAWSAccountId(accountId))
        } catch (ex: Exception) {
            throw AmazonSQSCommunicationException("Cannot get queue url with name $queueName and owner $accountId", ex)
        }

        return queueUrlResult.queueUrl
    }
}