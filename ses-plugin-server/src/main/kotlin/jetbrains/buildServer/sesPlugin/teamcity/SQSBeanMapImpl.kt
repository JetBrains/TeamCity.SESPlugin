

package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants

class SQSBeanMapImpl(private val data: Map<String, String>) : SQSBean {
    override fun isDisabled(): Boolean = !(data[Constants.ENABLED]?.toBoolean() ?: false)

    override val queueName: String
        get() = data[Constants.QUEUE_NAME_PARAM] ?: ""

    override val accountId: String
        get() = data[Constants.ACCOUNT_ID_PARAM] ?: ""

    override fun toMap(): Map<String, String> {
        return data
    }
}