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

package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.sesPlugin.data.ActionResult
import jetbrains.buildServer.sesPlugin.email.emailDisableDescription
import jetbrains.buildServer.sesPlugin.email.emailDisabled
import jetbrains.buildServer.sesPlugin.teamcity.util.GsonView
import jetbrains.buildServer.sesPlugin.teamcity.util.user
import jetbrains.buildServer.users.UserModel
import jetbrains.buildServer.web.openapi.*
import org.springframework.web.servlet.ModelAndView
import java.lang.IllegalArgumentException
import javax.servlet.http.HttpServletRequest

class UserProfileNoteController(private val userModel: UserModel, webManager: WebControllerManager, pagePlaces: PagePlaces, pluginDescriptor: PluginDescriptor)
    : SimplePageExtension(pagePlaces, PlaceId.ALL_PAGES_HEADER, "UserProfileNoteController", pluginDescriptor.getPluginResourcesPath("bouncedUserProfile.jsp")) {

    init {
        webManager.registerController("/admin/enableBouncedEmail.html") { req, _ ->
            val modelAndView = ModelAndView(GsonView())

            val currentUser = req.user()

            if (currentUser == null) {
                modelAndView.model["result"] = ActionResult(false, "No user id found")
                return@registerController modelAndView
            }

            if (!currentUser.isPermissionGrantedGlobally(Permission.CHANGE_USER)) {
                modelAndView.model["result"] = ActionResult(false, "Not permitted")
                return@registerController modelAndView
            }

            val userId = req.getParameter("userId")
            if (userId == null) {
                modelAndView.model["result"] = ActionResult(false, "No user id found")
                return@registerController modelAndView
            }

            val user = userModel.findUserById(userId.toLong())
            if (user == null) {
                modelAndView.model["result"] = ActionResult(false, "User not found")
                return@registerController modelAndView
            }

            user.emailDisabled = false
            user.emailDisableDescription = ""

            modelAndView.model["result"] = ActionResult()
            return@registerController modelAndView
        }
    }

    override fun isAvailable(request: HttpServletRequest) =
            request.servletPath == "/profile.html" || request.servletPath == "/admin/editUser.html"

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        val currentUser = request.user() ?: throw IllegalArgumentException()

        val editingUser = request.getParameter("userId")?.toLongOrNull()?.let { userModel.findUserById(it) } ?: currentUser

        model["canEdit"] = currentUser.isPermissionGrantedGlobally(Permission.CHANGE_USER)
        model["userId"] = editingUser.id
        model["isSameUser"] = currentUser == editingUser
        model["isDisabled"] = editingUser.emailDisabled
        model["disableDescription"] = editingUser.emailDisableDescription
    }
}