package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.users.SUser
import javax.servlet.http.HttpServletRequest

interface SessionUserProvider {
    fun getUser(request: HttpServletRequest): SUser?
}