package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.sesPlugin.email.emailDisableDescription
import jetbrains.buildServer.sesPlugin.email.emailDisabled
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.SimplePageExtension
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest


class BouncedUserNotificationController(pagePlaces: PagePlaces,
                                        pluginDescriptor: PluginDescriptor)
    : SimplePageExtension(pagePlaces, PlaceId.ALL_PAGES_HEADER, "BouncedUserNotificationController", pluginDescriptor.getPluginResourcesPath("bouncedUserNotification.jsp")) {

    override fun isAvailable(request: HttpServletRequest): Boolean = request.user()?.emailDisabled ?: false

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        model.put("disableDescription", request.user()!!.emailDisableDescription)
    }
}

fun HttpServletRequest.user(): SUser? = SessionUser.getUser(this)