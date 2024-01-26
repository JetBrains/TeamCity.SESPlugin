

package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.notification.email.EMailFilterResult
import jetbrains.buildServer.notification.email.EMailNotifierUserFilter
import jetbrains.buildServer.users.SUser

class BouncedUsersFilter(private val disabledEMailStateProvider: DisabledEMailStateProvider) : EMailNotifierUserFilter {
    override fun test(user: SUser) = if (disabledEMailStateProvider.isDisabled(user)) {
        EMailFilterResult(false, disabledEMailStateProvider.getEmailDisableDescription(user))
    } else {
        EMailFilterResult.OK
    }
}