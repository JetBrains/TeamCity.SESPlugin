package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotificationParseResult
import jetbrains.buildServer.sesPlugin.sqs.SQSNotificationParser
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions
import org.hamcrest.CustomMatcher
import org.jmock.Expectations.returnValue
import org.jmock.Expectations.throwException
import org.jmock.Mockery
import org.testng.annotations.Test

class ReceiveMessagesTaskTest {
    @Test
    fun testExceptionInCommunication() {
        mocking {
            val amazonSQS = mock(AmazonSQS::class)

            val initException = RuntimeException()
            invocation {
                on(amazonSQS)
                func("receiveMessage")
                will(throwException(initException))
            }

            val (messages, exception, _) = task().perform(amazonSQS, "someQueue")
            BDDAssertions.then(messages).isEmpty()
            BDDAssertions.then(exception).isSameAs(initException)

        }
    }

    @Test
    fun testReceive() {
        mocking {
            val amazonSQS = mock(AmazonSQS::class)
            val properties = mock(TeamCityProperties::class)
            val parser = mock(SQSNotificationParser::class)

            val messages = ReceiveMessageResult()
            messages.withMessages(Message().withBody("hi!"), Message().withBody("hi again!"))
            invocation {
                on(amazonSQS)
                func("receiveMessage")
                will(returnValue(messages))
            }
            invocation {
                on(parser)
                func(SQSNotificationParser::parse)
                count(2)
                will(returnValue(AmazonSQSNotificationParseResult("Hi there")))
            }
            check {
                one(properties).getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false); will(returnValue(false))
            }

            val (resMessages, exception, _) = task(properties, parser).perform(amazonSQS, "someQueue")
            BDDAssertions.then(resMessages).hasSize(2)
            BDDAssertions.then(resMessages[0].result).isEqualTo("Hi there")
            BDDAssertions.then(resMessages[1].result).isEqualTo("Hi there")
            BDDAssertions.then(exception).isNull()
        }
    }

    @Test
    fun testParse() {
        mocking {
            val amazonSQS = mock(AmazonSQS::class)
            val properties = mock(TeamCityProperties::class)
            val parser = mock(SQSNotificationParser::class)

            val messages = ReceiveMessageResult()
            messages.withMessages(Message().withBody("hi!"))
            invocation {
                on(amazonSQS)
                func("receiveMessage")
                will(returnValue(messages))
            }
            invocation {
                on(parser)
                func(SQSNotificationParser::parse)
                will(returnValue(AmazonSQSNotificationParseResult("Hi there")))
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return a[0] == "hi!"
                    }
                })
            }
            check {
                one(properties).getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false); will(returnValue(false))
            }

            val (resMessages, exception, _) = task(properties, parser).perform(amazonSQS, "someQueue")
            BDDAssertions.then(resMessages).hasSize(1)
            BDDAssertions.then(resMessages[0].result).isEqualTo("Hi there")
            BDDAssertions.then(exception).isNull()
        }
    }

    private fun Mockery.task(properties: TeamCityProperties = mock(TeamCityProperties::class),
                             parser: SQSNotificationParser = mock(SQSNotificationParser::class)) = ReceiveMessagesTask(properties, parser)
}