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

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.controllers.admin.AdminPage
import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.serverSide.crypt.RSACipher
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManagerImpl
import jetbrains.buildServer.sesPlugin.teamcity.util.DisabledUsersProvider
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import jetbrains.buildServer.web.openapi.Groupable
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import javax.servlet.http.HttpServletRequest

class EditSQSParamsAdminPage(pagePlaces: PagePlaces,
                             pluginDescriptor: PluginDescriptor,
                             private val sesIntegrationManager: SESIntegrationManagerImpl,
                             private val userSetProvider: DisabledUsersProvider,
                             private val properties: TeamCityProperties)
    : AdminPage(pagePlaces, "sesParams", pluginDescriptor.getPluginResourcesPath("editSESParams.jsp"), "Amazon SES Integration") {

    init {
        addJsFile(pluginDescriptor.getPluginResourcesPath("editSESParams.js"))
        addJsFile("/js/bs/testConnection.js")
        addCssFile(pluginDescriptor.getPluginResourcesPath("editSESParams.css"))
        setPosition(jetbrains.buildServer.web.openapi.PositionConstraint.between(mutableListOf("email"), mutableListOf("jabber")))
    }

    override fun isAvailable(request: HttpServletRequest) =
            checkHasGlobalPermission(request, Permission.CHANGE_SERVER_SETTINGS)

    override fun fillModel(model: MutableMap<String, Any>,
                           request: HttpServletRequest) {
        val beans = sesIntegrationManager.getBeans("")

        val propsBean = BasePropertiesBean(beans.firstOrNull()?.toMap() ?: mapOf("aws.credentials.type" to "aws.access.keys", "aws.use.default.credential.provider.chain" to "true"))

        model.put("propertiesBean", propsBean)

        val list = userSetProvider.users
        model.put("disabledUsers", list)

        model.put("publicKey", RSACipher.getHexEncodedPublicKey())
        model.put("debug", properties.getBoolean("teamcity.sesIntegration.enableDebug", false))
    }

    override fun getGroup() = Groupable.SERVER_RELATED_GROUP

}
