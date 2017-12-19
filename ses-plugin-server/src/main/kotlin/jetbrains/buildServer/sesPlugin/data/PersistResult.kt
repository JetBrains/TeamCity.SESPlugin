package jetbrains.buildServer.sesPlugin.data

data class PersistResult (
        val persisted: Boolean,
        val details: String,
        val exception: Exception? = null
)