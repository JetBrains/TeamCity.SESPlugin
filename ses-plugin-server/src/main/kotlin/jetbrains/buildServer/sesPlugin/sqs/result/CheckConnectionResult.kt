package jetbrains.buildServer.sesPlugin.sqs.result

data class CheckConnectionResult(
        val status: Boolean,
        val exception: Exception? = null,
        val description: String = "ok"
)