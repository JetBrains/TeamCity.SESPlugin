package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Expectations.throwException
import org.jmock.Mockery
import org.testng.annotations.Test

class SQSMessagesReceiverImplTest {
    @Test
    fun testReceiveDisabledBean() {
        mocking {
            val bean = mock(SQSBean::class)

            invocation(SQSBean::isDisabled) {
                on(bean)
                will(returnValue(true))
            }

            val (messages, exception, description) = receiver().receiveMessages(bean)
            then(messages).isEmpty()
            then(exception).isNull()
            then(description).isEqualTo("Disabled")
        }
    }

    @Test
    fun testCheckConnectionDisabledBean() {
        mocking {
            val bean = mock(SQSBean::class)

            invocation(SQSBean::isDisabled) {
                on(bean)
                will(returnValue(true))
            }

            val (status, exception, description) = receiver().checkConnection(bean)
            then(status).isFalse()
            then(exception).isNull()
            then(description).isEqualTo("Disabled")
        }
    }

    @Test
    fun testReceive() {
        mocking {
            val bean = mock(SQSBean::class)

            invocation(SQSBean::isDisabled) {
                on(bean)
                will(returnValue(false))
            }

            val awsClientsProvider = mock(AWSClientsProvider::class)

            val initialException = IllegalStateException("oops")

            invocation {
                func("withClient")
                on(awsClientsProvider)
                will(throwException(initialException))
            }

            val (messages, exception, description) = SQSMessagesReceiverImpl(mock(SQSNotificationParser::class), awsClientsProvider, mock(AmazonSQSClientFactory::class)).receiveMessages(bean)
            then(messages).isEmpty()
            then(exception).isNotNull().isSameAs(initialException)
            then(description).isEqualTo("No credentials provided")
        }
    }

    private fun Mockery.receiver(parser: SQSNotificationParser = mock(SQSNotificationParser::class),
                                 provider: AWSClientsProvider = mock(AWSClientsProvider::class),
                                 factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class)): SQSMessagesReceiverImpl = SQSMessagesReceiverImpl(parser, provider, factory)
}