package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import org.assertj.core.api.BDDAssertions.then
import org.testng.annotations.Test

class SQSBeanMapImplTest {
    @Test
    fun testIsDisabled() {
        then(SQSBeanMapImpl(mapOf()).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "false")).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "asdfas")).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "true")).isDisabled()).isFalse()
    }
}