

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.BounceNotification
import jetbrains.buildServer.sesPlugin.data.ComplaintNotification
import jetbrains.buildServer.sesPlugin.util.getResource
import org.assertj.core.api.BDDAssertions.then
import org.testng.annotations.Test

class SESNotificationParserImplTest {
    @Test
    fun testBounceNotification() {
        val data = getResource("bounce").asString()
        val parsed = SESNotificationParserImpl().parse(data)
        then(parsed).isInstanceOf(BounceNotification::class.java)
        then(parsed.eventType).isEqualTo("Bounce")
    }

    @Test
    fun testComplaintNotification() {
        val data = getResource("complaint").asString()
        val parsed = SESNotificationParserImpl().parse(data)
        then(parsed).isInstanceOf(ComplaintNotification::class.java)
        then(parsed.eventType).isEqualTo("Complaint")
    }
}