

package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.users.SUser

interface DisabledEMailStateProvider {
    fun disable(user: SUser, description: String)

    fun setDisabled(user: SUser, state: Boolean)

    fun isDisabled(user: SUser): Boolean

    fun setEmailDisableDescription(user: SUser, description: String)

    fun removeEmailDisableDescription(user: SUser)

    fun getEmailDisableDescription(user: SUser): String
}