package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.Recipient
import jetbrains.buildServer.sesPlugin.data.SESBounceNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.hamcrest.CustomMatcher
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.Test

class BounceMessageHandlerTest {
    @Test
    fun testAccepts() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            then(handler(bounceHandler).accepts("anything")).isFalse()
            then(handler(bounceHandler).accepts("Bounce")).isTrue()
        }
    }

    @Test
    fun testHandlePermanentBounce() {
        mocking {
            val bounce = mock(SESBounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounce).getBounceType(); will(Expectations.returnValue("Permanent"))
//                one(bounce).getBounceSubType(); will(Expectations.returnValue("Something")) // currently wrapped with logger callback
                one(bounce).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }
            invocation(BounceHandler::handleBounces) {
                on(bounceHandler)
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return (a[0] as Sequence<String>).first() == "theMail"
                    }
                })
            }

            handler(bounceHandler).handle(bounce)
        }
    }

    @Test
    fun testHandlePermanentBounceExceptionThrown() {
        mocking {
            val bounce1 = mock(SESBounceNotification::class, "bounce1")
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounce1).getBounceType(); will(Expectations.returnValue("Permanent"))
//                one(bounce1).getBounceSubType(); will(Expectations.returnValue("Something")) // currently wrapped with logger callback
                one(bounce1).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }
            invocation(BounceHandler::handleBounces) {
                on(bounceHandler)
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return (a[0] as Sequence<String>).first() == "theMail"
                    }
                })
                will(Expectations.throwException(RuntimeException("oups")))
            }

            handler(bounceHandler).handle(bounce1)
            // no exception thrown
        }
    }

    @Test
    fun testHandlePermanentBounceSuppressed() {
        mocking {
            val bounce = mock(SESBounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounce).getBounceType(); will(Expectations.returnValue("Permanent"))
//                one(bounce).getBounceSubType(); will(Expectations.returnValue("Suppressed")) // currently wrapped with logger callback
                one(bounce).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }
            invocation(BounceHandler::handleBounces) {
                on(bounceHandler)
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return (a[0] as Sequence<String>).first() == "theMail"
                    }
                })
            }

            handler(bounceHandler).handle(bounce)
        }
    }

    @Test
    fun testHandleNonCriticalBounce() {
        mocking {
            val bounce = mock(SESBounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            check {
                never(bounceHandler)
                one(bounce).getBounceType(); will(Expectations.returnValue("Transient"))
                one(bounce).getBounceSubType(); will(Expectations.returnValue("MessageTooLarge"))
                never(bounce).getRecipients()
            }

            handler(bounceHandler).handle(bounce)
        }
    }

    @Test
    fun testHandleUnknownBounce() {
        mocking {
            val bounce = mock(SESBounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            val logService = mock(LogService::class)
            check {
                never(bounceHandler)
                one(bounce).getBounceType(); will(Expectations.returnValue("UnknownValue"))
                never(bounce).getRecipients()
                one(logService)
            }

            BounceMessageHandler(bounceHandler, logService).handle(bounce)
        }
    }

    private fun Mockery.handler(handler: BounceHandler = mock(BounceHandler::class)): BounceMessageHandler = BounceMessageHandler(handler)
}