package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.notification.email.EMailFilterResult
import jetbrains.buildServer.notification.email.EMailNotifierUserFilter
import jetbrains.buildServer.users.SUser

class BouncedUsersFilter : EMailNotifierUserFilter {
    override fun test(user: SUser) = if (user.emailDisabled) {
        EMailFilterResult(false, user.emailDisableDescription)
    } else {
        EMailFilterResult.OK
    }
}