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

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.*
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Expectations.throwException
import org.testng.annotations.Test

@Suppress("UNCHECKED_CAST")
class SESMessagesReceiverTest {
    @Test
    fun testNoopOnException() {
        mocking {
            val sqsMessagesReceiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<AmazonSQSNotification>
            val sesNotificationParser = mock(SESNotificationParser::class)
            val bean = mock(SQSBean::class)

            val initException = Exception("some")
            val initDescription = "other"

            check {
                one(sqsMessagesReceiver).receiveMessages(bean); will(returnValue(AmazonSQSCommunicationResult<AmazonSQSNotification>(emptyList(), initException, initDescription)))
            }

            val receiver = SESMessagesReceiver(sqsMessagesReceiver, sesNotificationParser)

            val (messages, exception, description) = receiver.receiveMessages(bean)
            then(messages).isEmpty()
            then(exception).isSameAs(initException)
            then(description).isSameAs(initDescription)
        }
    }

    @Test
    fun testMessagesTransformed() {
        mocking {
            val sqsMessagesReceiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<AmazonSQSNotification>
            val sesNotificationParser = mock(SESNotificationParser::class)
            val bean = mock(SQSBean::class)

            val message = "someMessage"
            val notification = AmazonSQSNotification("", "", "", "", message, "", "", "", "", "")
            val data = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(sqsMessagesReceiver).receiveMessages(bean); will(returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(notification)))))
                one(sesNotificationParser).parse(message); will(returnValue(data))
            }

            val receiver = SESMessagesReceiver(sqsMessagesReceiver, sesNotificationParser)
            val (messages, exception, description) = receiver.receiveMessages(bean)

            then(exception).isNull()
            then(description).isEqualTo("ok")
            then(messages).hasSize(1)
            then(messages.first().result).isSameAs(data)
        }
    }

    @Test
    fun testParseExceptionHandled() {
        mocking {
            val sqsMessagesReceiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<AmazonSQSNotification>
            val sesNotificationParser = mock(SESNotificationParser::class)
            val bean = mock(SQSBean::class)

            val message1 = "someMessage"
            val message2 = "otherMessage"
            val notification1 = AmazonSQSNotification("", "", "", "", message1, "", "", "", "", "")
            val notification2 = AmazonSQSNotification("", "", "", "", message2, "", "", "", "", "")
            val data = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            val initException = Exception("some")

            check {
                one(sqsMessagesReceiver).receiveMessages(bean); will(returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(notification1), AmazonSQSNotificationParseResult(notification2)))))
                one(sesNotificationParser).parse(message1); will(throwException(initException))
                one(sesNotificationParser).parse(message2); will(returnValue(data))
            }

            val receiver = SESMessagesReceiver(sqsMessagesReceiver, sesNotificationParser)
            val (messages, exception, description) = receiver.receiveMessages(bean)

            then(exception).isNull()
            then(description).isEqualTo("ok")
            then(messages).hasSize(2)
            then(messages[0].result).isNull()
            then(messages[0].exception).isNotNull()
            then(messages[1].result).isSameAs(data)
            then(messages[1].exception).isNull()
        }
    }
}