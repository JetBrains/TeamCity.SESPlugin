

package jetbrains.buildServer.sesPlugin.data

data class CheckConnectionResult(
        val status: Boolean,
        val exception: Exception? = null,
        val description: String = "ok"
)