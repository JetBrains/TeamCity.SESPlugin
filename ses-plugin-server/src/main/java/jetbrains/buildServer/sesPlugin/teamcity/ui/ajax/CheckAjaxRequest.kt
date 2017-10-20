package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.sqs.SQSMessagesReceiver
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class CheckAjaxRequest(private val sqsMessagesReceiver: SQSMessagesReceiver,
                       private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {

    override val id: String = "check"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val bean = sesIntegrationManager.createFrom(data.properties)
        // todo validate?

        val checkConnectionResult = sqsMessagesReceiver.checkConnection(bean)

        return AjaxRequestResult(checkConnectionResult.status, checkConnectionResult.exception, checkConnectionResult.description)
    }
}