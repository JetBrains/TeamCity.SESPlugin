package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.sesPlugin.sqs.SQSMessagesReceiver
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class CheckAjaxRequest(private val sqsMessagesReceiver: SQSMessagesReceiver,
                       private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {
    override val id: String = "check"

    override fun handle(data: AjaxRequestData): AjaxRequestResult {
        val bean = sesIntegrationManager.getBeans(data.projectExtId)
        if (bean.isEmpty()) return AjaxRequestResult(false, null, "SQS queue is not configured")

        val checkConnectionResult = sqsMessagesReceiver.checkConnection(bean.first())

        return AjaxRequestResult(checkConnectionResult.status, checkConnectionResult.exception, checkConnectionResult.description)
    }
}