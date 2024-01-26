

package jetbrains.buildServer.sesPlugin.data

data class AmazonSQSCommunicationResult<out T>(
        val messages: List<AmazonSQSNotificationParseResult<T>>,
        val exception: Exception? = null,
        val description: String = "ok"
)