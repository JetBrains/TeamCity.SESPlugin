package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.impl.UserImpl

class DisabledEMailStateProviderImpl : DisabledEMailStateProvider {

    companion object {
        val DISABLED_PROPERTY_KEY = SimplePropertyKey("${UserImpl.PROTECTED_USER_PROPERTY_PREFIX}email.disabled")
        val DISABLE_DESCRIPTION_PROPERTY_KEY = SimplePropertyKey("${UserImpl.PROTECTED_USER_PROPERTY_PREFIX}email.disableDescription")
    }

    override fun disable(user: SUser, description: String) {
        setDisabled(user, true)
        setEmailDisableDescription(user, description)
    }

    override fun setDisabled(user: SUser, state: Boolean) {
        user.emailDisabled = state
    }

    override fun isDisabled(user: SUser): Boolean = user.emailDisabled

    override fun setEmailDisableDescription(user: SUser, description: String) {
        user.emailDisableDescription = description
    }

    override fun getEmailDisableDescription(user: SUser) = user.emailDisableDescription
}

var SUser.emailDisabled: Boolean
    set(state) = if (state) {
        setUserProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, state.toString())
    } else {
        deleteUserProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY)
    }
    get() = getBooleanProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY)
var SUser.emailDisableDescription: String
    set(description) = if (!description.isEmpty()) {
        setUserProperty(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY, description)
    } else {
        deleteUserProperty(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY)
    }
    get() = getPropertyValue(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY) ?: if (emailDisabled) "disabled" else "enabled"