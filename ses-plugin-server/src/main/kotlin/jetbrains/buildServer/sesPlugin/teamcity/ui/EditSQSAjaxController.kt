/*
 * Copyright 2000-2020 JetBrains s.r.o.
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

package jetbrains.buildServer.sesPlugin.teamcity.ui

import com.google.common.annotations.VisibleForTesting
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequest
import jetbrains.buildServer.sesPlugin.teamcity.util.GsonView
import jetbrains.buildServer.sesPlugin.teamcity.util.PluginPropertiesUtil
import jetbrains.buildServer.sesPlugin.teamcity.util.SessionUserProvider
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EditSQSAjaxController(private val ajaxRequests: List<AjaxRequest>,
                            private val webControllerManager: WebControllerManager,
                            private val sessionUserProvider: SessionUserProvider) : BaseController() {

    companion object {
        val URL = "/admin/editSQSParams.html"
    }

    fun init() {
        webControllerManager.registerController(URL, this)
    }

    @VisibleForTesting
    public override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val modelAndView = ModelAndView(GsonView())

        val user = sessionUserProvider.getUser(request)
        if (user == null) {
            modelAndView.model.put("result", AjaxRequestResult(false, "No user found"))
            return modelAndView
        }

        if (!user.isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS)) {
            modelAndView.model.put("result", AjaxRequestResult(false, "Not enough permissions"))
            return modelAndView
        }

        val res = doHandleInternal(request, response)

        modelAndView.model.put("result", res)

        return modelAndView
    }

    private fun doHandleInternal(request: HttpServletRequest, response: HttpServletResponse): AjaxRequestResult {
        val type = request.getParameter("type") ?: return AjaxRequestResult(false, "No action type provided")

        return ajaxRequests.find {
            it.id == type
        }?.let {
            val bean = BasePropertiesBean(HashMap())
            PluginPropertiesUtil.bindPropertiesFromRequest(request, bean, true)

            return@let it.handle(bean)
        } ?: AjaxRequestResult(false, "Unknown action $type")
    }
}