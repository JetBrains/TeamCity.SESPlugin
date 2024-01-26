

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.*
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.jmock.internal.Cardinality
import org.testng.annotations.Test

@Suppress("UNCHECKED_CAST")
class SQSMessagesReaderImplTest {
    @Test
    fun testReadAllQueuesChecksAllSources() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult<BounceNotification>(emptyList())))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult<BounceNotification>(emptyList())))
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf())

            then(reader.readAllQueues(sequenceOf(bean1, bean2)).countProcessed).isEqualTo(0)
        }
    }

    @Test
    fun testSumHandledMessages() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val message1 = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message2 = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message3 = BounceNotification("otherType", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))
            val message4 = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

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
                    count(Cardinality.ALLOWING)
                }

                invocation {
                    on(handler2)
                    func(SQSMessageHandler::handle)
                    count(Cardinality.ALLOWING)
                }
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean1, bean2)).countProcessed).isEqualTo(8)

        }
    }

    @Test
    fun testFirstHandlerAcceptingGetsTheMessage() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val handler1 = mock(SQSMessageHandler::class, "handler1")
            val handler2 = mock(SQSMessageHandler::class, "handler2")

            val bean = mock(SQSBean::class)

            val message = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler1).accepts("type"); will(Expectations.returnValue(true))
                never(handler2).accepts("type"); will(Expectations.returnValue(true))

                one(handler1).handle(message)
                never(handler2).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean)).countProcessed).isEqualTo(1)
        }
    }

    @Test
    fun testCorrectHandlerAcceptingGetsTheMessage() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val handler1 = mock(SQSMessageHandler::class, "handler1")
            val handler2 = mock(SQSMessageHandler::class, "handler2")

            val bean = mock(SQSBean::class)

            val message = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(
                    listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler1).accepts("type"); will(Expectations.returnValue(false))
                one(handler2).accepts("type"); will(Expectations.returnValue(true))

                never(handler1).handle(message)
                one(handler2).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler1, handler2))

            then(reader.readAllQueues(sequenceOf(bean)).countProcessed).isEqualTo(1)
        }
    }

    @Test
    fun testExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val handler = mock(SQSMessageHandler::class)

            val message = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult<BounceNotification>(emptyList(), Exception("some"))))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            then(reader.readAllQueues(sequenceOf(bean1, bean2)).countProcessed).isEqualTo(1)
        }
    }

    @Test
    fun testParseExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val bean1 = mock(SQSBean::class, "bean1")
            val bean2 = mock(SQSBean::class, "bean2")

            val handler = mock(SQSMessageHandler::class)

            val message = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            check {
                one(receiver).receiveMessages(bean1); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult<BounceNotification>(exception = SQSNotificationParseException("some"))))))
                one(receiver).receiveMessages(bean2); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message)
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            then(reader.readAllQueues(sequenceOf(bean1, bean2)).countProcessed).isEqualTo(1)

        }
    }

    @Test
    fun testHandleExceptionHandled() {
        mocking {
            val receiver = mock(SQSMessagesReceiver::class) as SQSMessagesReceiver<BounceNotification>

            val bean = mock(SQSBean::class)

            val handler = mock(SQSMessageHandler::class)

            val message = BounceNotification("type", BounceData("", "", emptyList(), "", "", ""), MailData("", "", "", "", "", emptyList(), false, emptyList()))

            val exception = Exception()
            check {
                one(receiver).receiveMessages(bean); will(Expectations.returnValue(AmazonSQSCommunicationResult(listOf(AmazonSQSNotificationParseResult(message)))))

                one(handler).accepts("type"); will(Expectations.returnValue(true))

                one(handler).handle(message); will(Expectations.throwException(exception))
            }

            val reader = SQSMessagesReaderImpl(receiver, listOf(handler))

            val res = reader.readAllQueues(sequenceOf(bean))
            then(res.countProcessed).isEqualTo(0)
            then(res.exception.isPresent).isFalse()

        }
    }
}