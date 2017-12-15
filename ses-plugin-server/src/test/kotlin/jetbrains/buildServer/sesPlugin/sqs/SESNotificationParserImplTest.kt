package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.util.getResource
import org.assertj.core.api.BDDAssertions.then
import org.testng.annotations.Test

class SESNotificationParserImplTest {
    @Test
    fun test() {
        val data = getResource("data").asString()
        val parsed = SESNotificationParserImpl().parse(data)
        then(parsed.eventType).isEqualTo("Bounce")
    }
}