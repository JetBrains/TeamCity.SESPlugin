package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.sqs.SQSBounceMessagesReader
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class GetMessagesAjaxRequest(private val sqsBounceMessagesService: SQSBounceMessagesReader,
                             private val sesIntegrationManager: SESIntegrationManager) : AjaxRequest {
    override val id = "receive"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val received = sqsBounceMessagesService.readAllQueues(sequenceOf(sesIntegrationManager.createFrom(data.properties)))
        return AjaxRequestResult(true, "Received $received messages")
    }
}