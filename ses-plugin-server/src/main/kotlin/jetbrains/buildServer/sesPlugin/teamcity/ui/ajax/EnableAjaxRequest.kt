package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.serverSide.ProjectManager
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants

class EnableAjaxRequest(private val manager: SESIntegrationManager,
                        private val projectManager: ProjectManager) : AjaxRequest {
    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        manager.setEnabled(projectManager.rootProject.projectId, data.properties[Constants.ENABLED]?.toBoolean() ?: true)
        return AjaxRequestResult(true)
    }

    override val id: String
        get() = "enable"
}