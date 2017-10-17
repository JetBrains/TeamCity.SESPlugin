package jetbrains.buildServer.sesPlugin.sqs

data class CheckConnectionResult(
        val status: Boolean,
        val exception: Exception? = null,
        val description: String = "ok"
)