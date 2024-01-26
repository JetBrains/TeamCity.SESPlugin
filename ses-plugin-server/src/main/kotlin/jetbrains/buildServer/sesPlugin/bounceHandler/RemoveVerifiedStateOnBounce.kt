

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.impl.UserImpl

class RemoveVerifiedStateOnBounce(private val sesIntegrationConfig: SESIntegrationConfig) : UserBounceHandler {
    companion object {
        val VERIFIED_EMAIL_PROPERTY_NAME_KEY = SimplePropertyKey(UserImpl.VERIFIED_EMAIL_PROPERTY_NAME)
    }

    override fun handleBounce(user: SUser) {
        if (sesIntegrationConfig.isDisableVerifiedMailOnBounce()) {
            user.deleteUserProperty(VERIFIED_EMAIL_PROPERTY_NAME_KEY)
        }
    }
}