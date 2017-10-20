package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean

interface AjaxRequest {
    val id: String
    fun handle(data: BasePropertiesBean): AjaxRequestResult
}