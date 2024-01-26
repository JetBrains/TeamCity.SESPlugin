

package jetbrains.buildServer.sesPlugin.teamcity.util

@Suppress("PropertyName")
class Constants {
    val ACCOUNT_ID_PARAM = static.ACCOUNT_ID_PARAM
    val ACCOUNT_ID_LABEL = static.ACCOUNT_ID_LABEL
    val QUEUE_NAME_PARAM = static.QUEUE_NAME_PARAM
    val QUEUE_NAME_LABEL = static.QUEUE_NAME_LABEL
    val ENABLED = static.ENABLED

    companion object static {
        val ACCOUNT_ID_PARAM = "aws.sesIntegration.accountId"
        val ACCOUNT_ID_LABEL = "Owner Account ID"
        val QUEUE_NAME_PARAM = "aws.sesIntegration.queueName"
        val QUEUE_NAME_LABEL = "SQS Queue Name"
        val ENABLED = "aws.sesIntegration.enabled"
    }
}