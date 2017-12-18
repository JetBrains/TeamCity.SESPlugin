package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.data.BounceData
import jetbrains.buildServer.sesPlugin.sqs.data.MailData
import jetbrains.buildServer.sesPlugin.sqs.data.SESNotificationData
import jetbrains.buildServer.sesPlugin.sqs.result.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.sqs.result.AmazonSQSNotificationParseResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.testng.annotations.Test

@Suppress("UNCHECKED_CAST")
class SQSMessagesReaderImplTest {
    @Test
    fun testReadAllQueuesChecksAllSources() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult<SESNotificationData>(emptyList())))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult<SESNotificationData>(emptyList())))
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf())

            then(reader.readAllQueues(sequenceOf(bean1, bean2))).isEqualTo(0)
        }
    }

    @Test
    fun testSumHandledMessages() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val message1 = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message2 = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message3 = SESNotificationData("otherType", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message4 = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            val handler1 = mock(SQSMessageHandler::class, "handler1")
            val handler2 = mock(SQSMessageHandler::class, "handler2")

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message1), AmazonSQSNotificationParseResult(message2), AmazonSQSNotificationParseResult(message3), AmazonSQSNotificationParseResult(message4)))))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message1), AmazonSQSNotificationParseResult(message2), AmazonSQSNotificationParseResult(message3), AmazonSQSNotificationParseResult(message4)))))

                allowing(handler1).accepts("type"); will(Expectations.returnValue(false))
                allowing(handler2).accepts("type"); will(Expectations.returnValue(true))
                allowing(handler1).accepts("otherType"); will(Expectations.returnValue(true))
                allowing(handler2).accepts("otherType"); will(Expectations.returnValue(false))

                invocation {
                    on(handler1)
                    func(SQSMessageHandler::handle)
                }

                invocation {
                    on(handler2)
                    func(SQSMessageHandler::handle)
                }
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean1, bean2))).isEqualTo(8)

        }
    }

    @Test
    fun testFirstHandlerAcceptingGetsTheMessage() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val handler1 = mock(SQSMessageHandler::class, "handler1")
            val handler2 = mock(SQSMessageHandler::class, "handler2")

            val bean = mock(SQSBean::class)

            val message = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler1).accepts("type"); will(Expectations.returnValue(true))
                never(handler2).accepts("type"); will(Expectations.returnValue(true))

                one(handler1).handle(message)
                never(handler2).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean))).isEqualTo(1)
        }
    }

    @Test
    fun testCorrectHandlerAcceptingGetsTheMessage() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val handler1 = mock(SQSMessageHandler::class, "handler1")
            val handler2 = mock(SQSMessageHandler::class, "handler2")

            val bean = mock(SQSBean::class)

            val message = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler1).accepts("type"); will(Expectations.returnValue(false))
                one(handler2).accepts("type"); will(Expectations.returnValue(true))

                never(handler1).handle(message)
                one(handler2).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean))).isEqualTo(1)
        }
    }

    @Test
    fun testExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val handler = mock(SQSMessageHandler::class)

            val message = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult<SESNotificationData>(emptyList(), Exception("some"))))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            then(reader.readAllQueues(sequenceOf(bean1, bean2))).isEqualTo(1)
        }
    }

    @Test
    fun testParseExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val handler = mock(SQSMessageHandler::class)

            val message = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult<SESNotificationData>(exception = SQSNotificationParseException("some"))))))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            then(reader.readAllQueues(sequenceOf(bean1, bean2))).isEqualTo(1)

        }
    }

    @Test
    fun testHandleExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<SESNotificationData>

            val bean = mock(SQSBean::class)

            val handler = mock(SQSMessageHandler::class)

            val message = SESNotificationData("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message); will(Expectations.throwException(Exception()))
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            then(reader.readAllQueues(sequenceOf(bean))).isEqualTo(0)

        }
    }
}