package jetbrains.buildServer.sesPlugin.sqs

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueUrlResult
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.QueueUrlProviderImpl
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Expectations.throwException
import org.testng.annotations.Test

class QueueUrlProviderImplTest {
    @Test
    fun testGet() {
        mocking {
            val sqs = mock(AmazonSQS::class)
            val bean = mock(SQSBean::class)

            check {
                one(bean).queueName; will(returnValue("aaa"))
                one(bean).accountId; will(returnValue("bbb"))
            }
            invocation {
                on(sqs)
                func("getQueueUrl")
                will(returnValue(GetQueueUrlResult().withQueueUrl("some")))
            }

            then(QueueUrlProviderImpl().getQueueUrl(sqs, bean)).isEqualTo("some")
        }
    }

    @Test
    fun testError() {
        mocking {
            val sqs = mock(AmazonSQS::class)
            val bean = mock(SQSBean::class)

            val initialException = IllegalStateException()

            check {
                one(bean).queueName; will(returnValue("aaa"))
                one(bean).accountId; will(throwException(initialException))
            }

            jetbrains.buildServer.sesPlugin.util.then { (QueueUrlProviderImpl().getQueueUrl(sqs, bean)) }.isSameAs(initialException)
        }
    }
}