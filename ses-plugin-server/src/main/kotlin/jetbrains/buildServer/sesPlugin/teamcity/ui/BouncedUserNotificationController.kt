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

import jetbrains.buildServer.server.responsible.ResponsibleContactProvider
import jetbrains.buildServer.sesPlugin.email.emailDisableDescription
import jetbrains.buildServer.sesPlugin.email.emailDisabled
import jetbrains.buildServer.sesPlugin.teamcity.util.user
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.SimplePageExtension
import javax.servlet.http.HttpServletRequest


class BouncedUserNotificationController(pagePlaces: PagePlaces,
                                        pluginDescriptor: PluginDescriptor,
                                        private val responsibleContact: ResponsibleContactProvider)
    : SimplePageExtension(pagePlaces, PlaceId.ALL_PAGES_HEADER, "BouncedUserNotificationController", pluginDescriptor.getPluginResourcesPath("bouncedUserNotification.jsp")) {

    override fun isAvailable(request: HttpServletRequest): Boolean = request.user()?.emailDisabled ?: false

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        model.put("disableDescription", request.user()!!.emailDisableDescription)

        val globalResponsible = responsibleContact.getGlobal()
        if (globalResponsible.isPresent) {
            model.put("contactInfo", globalResponsible.get())
        } else {
            model.put("contactInfo", "Contact your TeamCity administrator for details")
        }
    }
}