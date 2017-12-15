package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.Test

class AmazonSQSCommunicatorImplTest {
    @Test
    fun testReceive() {
        mocking {
            val bean = mock(SQSBean::class)

            invocation(SQSBean::isDisabled) {
                on(bean)
                will(Expectations.returnValue(false))
            }

            val awsClientsProvider = mock(AWSClientsProvider::class)

            val initialException = IllegalStateException("oops")

            invocation {
                func("withClient")
                on(awsClientsProvider)
                will(Expectations.throwException(initialException))
            }

            then({ communicator(awsClientsProvider).withCommunication(bean) { "" } }).isSameAs(initialException)
        }
    }

    private fun Mockery.communicator(provider: AWSClientsProvider = mock(AWSClientsProvider::class),
                                     factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class)): AmazonSQSCommunicator = AmazonSQSCommunicatorImpl(provider, factory)
}