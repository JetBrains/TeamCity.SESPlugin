/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

            AjaxRequestResult(checkConnectionResult.status, if (checkConnectionResult.status) "" else checkConnectionResult.description, checkConnectionResult.exception)
        } catch (e: Exception) {
            AjaxRequestResult(false, "Error occurred: ${e.message}", e)
        }

    }
}