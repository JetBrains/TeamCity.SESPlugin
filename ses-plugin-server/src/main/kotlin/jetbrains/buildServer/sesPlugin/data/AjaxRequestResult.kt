package jetbrains.buildServer.sesPlugin.data

import jetbrains.buildServer.sesPlugin.teamcity.util.JsonModelComponent

data class AjaxRequestResult(
        val successful: Boolean,
        val description: String? = null,
        val exception: Exception? = null,
        val errorFields: Collection<String> = emptyList()
) : JsonModelComponent