package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.sqs.SQSMessagesReader
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import jetbrains.buildServer.sesPlugin.teamcity.SQSBeanValidator

class GetMessagesAjaxRequest(private val sqsBounceMessagesService: SQSMessagesReader,
                             private val sesIntegrationManager: SESIntegrationManager,
                             private val beanValidator: SQSBeanValidator) : AjaxRequest {
    override val id = "receive"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val bean = sesIntegrationManager.createFrom(data.properties)

        val validate = beanValidator.validate(bean)
        if (!validate.status) {
            return AjaxRequestResult(false, "All mandatory fields should be filled: ${validate.errorFields}", null, validate.errorFields)
        }

        val received = sqsBounceMessagesService.readAllQueues(sequenceOf(bean))
        return AjaxRequestResult(received.successful, received.description)
    }
}