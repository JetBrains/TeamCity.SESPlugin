package jetbrains.buildServer.sesPlugin.sqs

data class AmazonSQSNotification(
        val type: String,
        val message: String
)