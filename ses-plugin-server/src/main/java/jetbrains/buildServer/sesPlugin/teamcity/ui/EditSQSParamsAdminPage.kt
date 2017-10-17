package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.controllers.admin.AdminPage
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
//        val bean = sesIntegrationManager.getBean()

//        val propsBean = BasePropertiesBean(bean.toMap())

//        PluginPropertiesUtil.bindPropertiesFromRequest(request, propsBean, true)

//        model.put("propertiesBean", propsBean)
    }

    override fun getGroup() = Groupable.SERVER_RELATED_GROUP
}