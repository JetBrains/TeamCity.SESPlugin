package jetbrains.buildServer.sesPlugin.teamcity

data class PersistResult (
        val persisted: Boolean,
        val details: String,
        val exception: Exception? = null
)