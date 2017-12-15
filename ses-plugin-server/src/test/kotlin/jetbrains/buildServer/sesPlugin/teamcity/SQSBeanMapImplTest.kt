package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.sesPlugin.util.then
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

    @Test
    fun testAccountId() {
        then(SQSBeanMapImpl(mapOf(Constants.ACCOUNT_ID_PARAM to "account")).accountId).isEqualTo("account")
        then(SQSBeanMapImpl(mapOf(Constants.ACCOUNT_ID_PARAM to "account1")).accountId).isEqualTo("account1")
        then({ SQSBeanMapImpl(mapOf()).accountId }).isNotNull().isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun testQueueName() {
        then(SQSBeanMapImpl(mapOf(Constants.QUEUE_NAME_PARAM to "queue")).queueName).isEqualTo("queue")
        then(SQSBeanMapImpl(mapOf(Constants.QUEUE_NAME_PARAM to "queue1")).queueName).isEqualTo("queue1")
        then({ SQSBeanMapImpl(mapOf()).queueName }).isNotNull().isInstanceOf(IllegalStateException::class.java)
    }
}