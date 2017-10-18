package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.sesPlugin.teamcity.util.JsonModelComponent

data class AjaxRequestResult(
        val status: Boolean,
        val exception: Exception? = null,
        val description: String? = null
) : JsonModelComponent