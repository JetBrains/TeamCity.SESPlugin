package jetbrains.buildServer.sesPlugin.teamcity.ui.health

import jetbrains.buildServer.sesPlugin.teamcity.health.DisabledMailsReport
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.healthStatus.HealthStatusItemPageExtension
import javax.servlet.http.HttpServletRequest

class DisabledMailsPageExtension(pluginDescriptor: PluginDescriptor,
                                 pagePlaces: PagePlaces) : HealthStatusItemPageExtension(DisabledMailsReport.id, pagePlaces) {
    init {
        includeUrl = pluginDescriptor.getPluginResourcesPath("/healthReport/blockedUsers.jsp")
    }

    override fun isAvailable(request: HttpServletRequest): Boolean {
        if (!super.isAvailable(request)) return false

        val count = getStatusItem(request).additionalData[DisabledMailsReport.countKey] as Int? ?: 0

        return count > 0
    }

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        val count = getStatusItem(request).additionalData[DisabledMailsReport.countKey] as Int? ?: 0

        model[DisabledMailsReport.countKey] = count
    }
}