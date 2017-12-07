package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.sesPlugin.teamcity.util.JsonModelComponent

data class AjaxRequestResult(
        val successful: Boolean,
        val description: String? = null,
        val exception: Exception? = null
) : JsonModelComponent