package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.sqs.SQSBounceMessagesReader
import jetbrains.buildServer.sesPlugin.teamcity.SESBeanMapImpl

class GetMessagesAjaxRequest(private val sqsBounceMessagesService: SQSBounceMessagesReader) : AjaxRequest {
    override val id = "receive"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val received = sqsBounceMessagesService.readAllQueues(sequenceOf(SESBeanMapImpl(data.properties)))
        return AjaxRequestResult(true, "Received $received messages")
    }
}