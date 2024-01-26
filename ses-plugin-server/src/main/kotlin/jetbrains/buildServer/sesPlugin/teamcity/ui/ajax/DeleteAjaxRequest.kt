

package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class DeleteAjaxRequest(private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {

    override val id = "delete"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val properties = data.properties

        val id = properties["projectExtId"] ?: return AjaxRequestResult(false, "Project ID should be provided")

        val deleteBean = sesIntegrationManager.deleteBean(id)

        return AjaxRequestResult(deleteBean.persisted, deleteBean.details, deleteBean.exception)
    }
}