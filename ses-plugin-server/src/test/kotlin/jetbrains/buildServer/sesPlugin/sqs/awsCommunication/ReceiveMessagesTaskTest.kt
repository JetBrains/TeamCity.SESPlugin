/*
 * Copyright 2000-2020 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

@Suppress("UNCHECKED_CAST")
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
            val properties = mock(TeamCityProperties::class)
            check {
                one(properties).getInt("teamcity.sesIntegration.maxNumberOfMessages", 10); will(returnValue(10))
            }

            val (messages, exception, _) = task(properties).perform(amazonSQS, "someQueue")
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
                one(properties).getBoolean("teamcity.sesIntegration.deleteReadMessages", true); will(returnValue(false))
                one(properties).getInt("teamcity.sesIntegration.maxNumberOfMessages", 10); will(returnValue(10))
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
                one(properties).getBoolean("teamcity.sesIntegration.deleteReadMessages", true); will(returnValue(false))
                one(properties).getInt("teamcity.sesIntegration.maxNumberOfMessages", 10); will(returnValue(10))
            }

            val (resMessages, exception, _) = task(properties, parser).perform(amazonSQS, "someQueue")
            BDDAssertions.then(resMessages).hasSize(1)
            BDDAssertions.then(resMessages[0].result).isEqualTo("Hi there")
            BDDAssertions.then(exception).isNull()
        }
    }

    @Test
    fun testDelete() {
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
            invocation {
                on(amazonSQS)
                func("deleteMessageBatch")
            }
            check {
                one(properties).getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false); will(returnValue(false))
                one(properties).getBoolean("teamcity.sesIntegration.deleteReadMessages", true); will(returnValue(true))
                one(properties).getInt("teamcity.sesIntegration.maxNumberOfMessages", 10); will(returnValue(10))
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