package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.controllers.admin.AdminPage
import jetbrains.buildServer.serverSide.crypt.RSACipher
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManagerImpl
import jetbrains.buildServer.web.openapi.Groupable
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import javax.servlet.http.HttpServletRequest

class EditSQSParamsAdminPage(pagePlaces: PagePlaces,
                             pluginDescriptor: PluginDescriptor,
                             private val sesIntegrationManager: SESIntegrationManagerImpl)
    : AdminPage(pagePlaces, "sesParams", pluginDescriptor.getPluginResourcesPath("editSESParams.jsp"), "Amazon SES Integration") {

    init {
        addJsFile(pluginDescriptor.getPluginResourcesPath("editSESParams.js"))
        addCssFile(pluginDescriptor.getPluginResourcesPath("editSESParams.css"))
    }

    fun init() = register()

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        val beans = sesIntegrationManager.getBeans("")

        val propsBean = if (beans.isEmpty()) {
            BasePropertiesBean(mapOf("aws.credentials.type" to "aws.access.keys", "aws.use.default.credential.provider.chain" to "true"))
        } else {
            BasePropertiesBean(beans[0].toMap())
        }

        model.put("publicKey", RSACipher.getHexEncodedPublicKey())
        model.put("propertiesBean", propsBean)
    }

    override fun getGroup() = Groupable.SERVER_RELATED_GROUP
}
