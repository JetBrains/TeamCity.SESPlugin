package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.Recipient
import jetbrains.buildServer.sesPlugin.data.SESComplaintNotification
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.hamcrest.CustomMatcher
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.testng.annotations.Test

class ComplaintMessageHandlerTest {
    @Test
    fun testAccept() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            then(handler(bounceHandler).accepts("asdf")).isFalse()
            then(handler(bounceHandler).accepts("Complaint")).isTrue()
        }
    }

    @Test
    fun testHandle() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            val data = mock(SESComplaintNotification::class)
            check {
                one(data).getComplainedRecipients(); will(returnValue(sequenceOf(Recipient("mail", "no", "bad", "code"))))
            }
            invocation(BounceHandler::handleBounces) {
                on(bounceHandler)
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return (a[0] as Sequence<String>).first() == "mail"
                    }
                })
            }

            handler(bounceHandler).handle(data)
        }
    }

    private fun Mockery.handler(bounceHandler: BounceHandler = mock(BounceHandler::class)) = ComplaintMessageHandler(bounceHandler)
}