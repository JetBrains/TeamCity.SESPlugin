package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.*
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.jmock.api.Invocation
import org.jmock.lib.action.ActionSequence
import org.jmock.lib.action.CustomAction
import org.testng.annotations.Test

@Suppress("UNCHECKED_CAST")
class AmazonSQSCommunicatorImplTest {
    @Test
    fun testPerformTask() {
        mocking {
            val bean = mock(SQSBean::class)
            val task = mock(AmazonSQSCommunicatorTask::class) as AmazonSQSCommunicatorTask<Any>

            val provider: AWSClientsProvider = mock(AWSClientsProvider::class)
            val factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class)
            val queueUrl: QueueUrlProvider = mock(QueueUrlProvider::class)

            val clients = mock(SQSAWSClients::class)
            val amazonSQS = mock(AutoCloseableAmazonSQS::class)

            val result = "The result"

            check {
                one(bean).isDisabled(); will(returnValue(false))
                one(factory).createAmazonSQSClient(clients); will(returnValue(amazonSQS))
                one(queueUrl).getQueueUrl(amazonSQS, bean); will(returnValue("someQueue"))

                one(task).perform(amazonSQS, "someQueue"); will(returnValue(result))
                one(amazonSQS).close()
            }
            invocation {
                func("withClient")
                on(provider)
                will(
                        ActionSequence(
                                object : CustomAction("call the callback") {
                                    override fun invoke(p0: Invocation?): Any {
                                        if (p0 != null) {
                                            return (p0.getParameter(1) as Function1<SQSAWSClients, String>).invoke(clients)
                                        } else {
                                            throw RuntimeException("")
                                        }
                                    }
                                }, returnValue(clients)
                        )

                )
                count(1)
            }

            BDDAssertions.then(receiver(provider, factory, queueUrl).performTask(bean, task)).isSameAs(result)
        }
    }

    private fun Mockery.receiver(provider: AWSClientsProvider = mock(AWSClientsProvider::class),
                                 factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class),
                                 queueUrl: QueueUrlProvider = mock(QueueUrlProvider::class)): AmazonSQSCommunicatorImpl = AmazonSQSCommunicatorImpl(provider, factory, queueUrl)
}