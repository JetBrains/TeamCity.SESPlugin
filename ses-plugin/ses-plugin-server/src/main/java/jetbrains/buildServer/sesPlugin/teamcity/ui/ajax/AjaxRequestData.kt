package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import com.google.gson.annotations.SerializedName

data class AjaxRequestData(
        @SerializedName("type") val type: String = "unknown",
        @SerializedName("projectExtId") val projectExtId: String,
        @SerializedName("params") val params: Map<String, String> = emptyMap()
)