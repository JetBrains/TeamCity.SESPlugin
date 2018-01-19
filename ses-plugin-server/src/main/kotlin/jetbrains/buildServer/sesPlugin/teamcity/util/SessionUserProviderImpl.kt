package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest

class SessionUserProviderImpl : SessionUserProvider {
    override fun getUser(request: HttpServletRequest): SUser?
            = SessionUser.getUser(request)
}