

package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.util.SessionUser
import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.user(): SUser? = SessionUser.getUser(this)