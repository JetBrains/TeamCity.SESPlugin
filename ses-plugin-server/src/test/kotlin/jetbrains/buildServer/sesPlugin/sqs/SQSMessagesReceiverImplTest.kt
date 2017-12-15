package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
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

            val (messages, exception, description) = SQSMessagesReceiverImpl(mock(SQSNotificationParser::class)).receiveMessages(bean)
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

            val (status, exception, description) = SQSMessagesReceiverImpl(mock(SQSNotificationParser::class)).checkConnection(bean)
            then(status).isFalse()
            then(exception).isNull()
            then(description).isEqualTo("Disabled")
        }
    }
}