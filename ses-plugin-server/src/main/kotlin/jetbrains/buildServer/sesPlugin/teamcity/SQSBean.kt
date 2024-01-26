

package jetbrains.buildServer.sesPlugin.teamcity

interface SQSBean {
    fun toMap(): Map<String, String>

    fun isDisabled(): Boolean

    val queueName: String

    val accountId: String
}