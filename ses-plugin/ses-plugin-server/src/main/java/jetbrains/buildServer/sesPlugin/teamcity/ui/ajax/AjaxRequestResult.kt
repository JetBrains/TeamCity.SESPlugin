package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

data class AjaxRequestResult(
        val status: Boolean,
        val exception: Exception? = null,
        val description: String? = null
)