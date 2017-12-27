package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.Recipient
import jetbrains.buildServer.sesPlugin.data.SESComplaintNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.testng.annotations.Test

class ComplaintMessageHandlerTest {
    @Test
    fun testAccept() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            then(handler(bounceHandler).accepts("asdf")).isFalse()
            then(handler(bounceHandler).accepts("Complaint")).isFalse()
        }
    }

    @Test
    fun testHandle() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            val data = mock(SESComplaintNotification::class)
            check {
                one(bounceHandler).handleBounce("mail")
                one(data).getComplainedRecipients(); will(returnValue(sequenceOf(Recipient("mail", "no", "bad", "code"))))
            }
            handler(bounceHandler).handle(data)
        }
    }

    private fun Mockery.handler(bounceHandler: BounceHandler = mock(BounceHandler::class), logService: LogService = mock(LogService::class)) = ComplaintMessageHandler(bounceHandler, logService)
}