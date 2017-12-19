package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.Recipient
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.jmock.Mockery
import org.testng.annotations.Test

class SESBounceMessageHandlerTest {
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
            val bounce = mock(BounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounceHandler).handleBounce("theMail")
                one(bounce).getBounceType(); will(Expectations.returnValue("Permanent"))
                one(bounce).getBounceSubType(); will(Expectations.returnValue("Something"))
                one(bounce).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }

            handler(bounceHandler).handle(bounce)
        }
    }

    @Test
    fun testHandlePermanentBounceExceptionThrown() {
        mocking {
            val bounce1 = mock(BounceNotification::class, "bounce1")
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounceHandler).handleBounce("theMail"); will(Expectations.throwException(RuntimeException("oups")))
                one(bounce1).getBounceType(); will(Expectations.returnValue("Permanent"))
                one(bounce1).getBounceSubType(); will(Expectations.returnValue("Something"))
                one(bounce1).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }

            handler(bounceHandler).handle(bounce1)
            // no exception thrown
        }
    }

    @Test
    fun testHandlePermanentBounceSuppressed() {
        mocking {
            val bounce = mock(BounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            check {
                one(bounceHandler).handleBounce("theMail")
                one(bounce).getBounceType(); will(Expectations.returnValue("Permanent"))
                one(bounce).getBounceSubType(); will(Expectations.returnValue("Suppressed"))
                one(bounce).getRecipients(); will(Expectations.returnValue(listOf(Recipient("theMail", "failed", "", ""))))
            }

            handler(bounceHandler).handle(bounce)
        }
    }

    @Test
    fun testHandleNonCriticalBounce() {
        mocking {
            val bounce = mock(BounceNotification::class)
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
            val bounce = mock(BounceNotification::class)
            val bounceHandler = mock(BounceHandler::class)
            val logService = mock(LogService::class)
            check {
                never(bounceHandler)
                one(bounce).getBounceType(); will(Expectations.returnValue("UnknownValue"))
                never(bounce).getRecipients()
                one(logService)
            }

            SESBounceMessageHandler(bounceHandler, logService).handle(bounce)
        }
    }

    private fun Mockery.handler(handler: BounceHandler = mock(BounceHandler::class)): SESBounceMessageHandler = SESBounceMessageHandler(handler)
}