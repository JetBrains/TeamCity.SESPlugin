package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class SubmitAjaxRequest(private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {

    override val id: String = "submit"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val properties = data.properties

        val id = properties["projectExtId"] ?: return AjaxRequestResult(false, "Project ID should be provided")

        properties.values.removeIf({ it.isNullOrBlank() })
        properties.values.remove("projectExtId")

        val persistResult = sesIntegrationManager.persistBean(sesIntegrationManager.createFrom(properties), id)

        return AjaxRequestResult(persistResult.persisted, persistResult.details)
    }
}