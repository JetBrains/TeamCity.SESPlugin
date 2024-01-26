

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