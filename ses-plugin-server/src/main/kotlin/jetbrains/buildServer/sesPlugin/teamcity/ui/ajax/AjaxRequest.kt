package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult

interface AjaxRequest {
    val id: String
    fun handle(data: BasePropertiesBean): AjaxRequestResult
}