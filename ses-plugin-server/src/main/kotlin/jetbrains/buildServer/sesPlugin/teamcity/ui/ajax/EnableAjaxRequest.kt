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