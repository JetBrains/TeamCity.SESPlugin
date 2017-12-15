package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants

class SQSBeanMapImpl(private val data: Map<String, String>) : SQSBean {
    override fun isDisabled(): Boolean = !(data[Constants.ENABLED]?.toBoolean() ?: false)

    override fun toMap(): Map<String, String> {
        return data
    }
}