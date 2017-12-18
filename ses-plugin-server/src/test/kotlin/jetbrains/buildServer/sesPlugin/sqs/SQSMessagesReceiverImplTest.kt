package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.AmazonSQSCommunicator
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
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

    private fun Mockery.receiver(parser: SQSNotificationParser = mock(SQSNotificationParser::class),
                                 communicator: AmazonSQSCommunicator = mock(AmazonSQSCommunicator::class)): SQSMessagesReceiverImpl = SQSMessagesReceiverImpl(parser, communicator)
}