package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.jmock.api.Invocation
import org.jmock.lib.action.ActionSequence
import org.jmock.lib.action.CustomAction
import org.testng.annotations.Test

class AmazonSQSCommunicatorImplTest {
    @Test
    fun testReceive() {
        mocking {
            val bean = mock(SQSBean::class)
            val awsClientsProvider = mock(AWSClientsProvider::class)
            val clients = mock(SQSAWSClients::class)
            val factory = mock(AmazonSQSClientFactory::class)
            val getter = mock(QueueUrlProvider::class)
            val amazonSQS = mock(AutoCloseableAmazonSQS::class)

            val queueUrl = "TheQueueUrl"

            check {
                one(factory).createAmazonSQSClient(clients); will(returnValue(amazonSQS))
                one(getter).getQueueUrl(amazonSQS, bean); will(returnValue(queueUrl))
                one(amazonSQS).close()
            }
            invocation {
                func("withClient")
                on(awsClientsProvider)
                will(
                        ActionSequence(
                                object : CustomAction("call the callback") {
                                    override fun invoke(p0: Invocation?): Any {
                                        if (p0 != null) {
                                            (p0.getParameter(1) as kotlin.Function1<SQSAWSClients, String>).invoke(clients)

                                        }
                                        return Object()
                                    }
                                }, returnValue(clients)
                        )

                )
                count(1)
            }

            communicator(awsClientsProvider, factory, getter).withCommunication(bean) {
                then(it.queueUrl).isEqualTo(queueUrl)
                then(it.amazonSQS).isSameAs(amazonSQS)
            }
        }
    }

    private fun Mockery.communicator(provider: AWSClientsProvider = mock(AWSClientsProvider::class),
                                     factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class),
                                     getter: QueueUrlProvider = mock(QueueUrlProvider::class)): AmazonSQSCommunicator = AmazonSQSCommunicatorImpl(provider, factory, getter)
}