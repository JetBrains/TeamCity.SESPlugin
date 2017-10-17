package jetbrains.buildServer.sesPlugin.teamcity.util

class Constants {
    val ACCOUNT_ID_PARAM = static.ACCOUNT_ID_PARAM
    val QUEUE_NAME_PARAM = static.QUEUE_NAME_PARAM

    companion object static {
        val ACCOUNT_ID_PARAM = "aws.sesIntegration.accountId"
        val QUEUE_NAME_PARAM = "aws.sesIntegration.queueName"
    }
}