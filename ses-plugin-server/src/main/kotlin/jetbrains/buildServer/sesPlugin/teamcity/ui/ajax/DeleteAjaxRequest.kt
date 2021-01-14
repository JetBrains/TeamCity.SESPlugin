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