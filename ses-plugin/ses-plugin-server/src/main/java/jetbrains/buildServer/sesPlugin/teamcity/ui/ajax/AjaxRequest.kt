package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

interface AjaxRequest {
    val id: String
    fun handle(data: AjaxRequestData): AjaxRequestResult
}