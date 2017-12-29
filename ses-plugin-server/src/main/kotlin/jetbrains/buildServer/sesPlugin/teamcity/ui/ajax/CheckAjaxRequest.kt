package jetbrains.buildServer.sesPlugin.teamcity.ui.ajax

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.sqs.SQSConnectionChecker
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import jetbrains.buildServer.sesPlugin.teamcity.SQSBeanValidator

class CheckAjaxRequest(private val sqsMessagesReceiver: SQSConnectionChecker,
                       private val sesIntegrationManager: SESIntegrationManager,
                       private val beanValidator: SQSBeanValidator) : AjaxRequest {

    override val id: String = "check"

    override fun handle(data: BasePropertiesBean): AjaxRequestResult {
        val bean = sesIntegrationManager.createFrom(data.properties)

        val validate = beanValidator.validate(bean)
        if (!validate.status) {
            return AjaxRequestResult(false, "All mandatory fields should be filled: ${validate.errorFields}", null, validate.errorFields)
        }

        return try {
            val checkConnectionResult = sqsMessagesReceiver.checkConnection(bean)

            AjaxRequestResult(checkConnectionResult.status, checkConnectionResult.description, checkConnectionResult.exception)
        } catch (e: Exception) {
            AjaxRequestResult(false, "Error occurred: ${e.message}", e)
        }

    }
}