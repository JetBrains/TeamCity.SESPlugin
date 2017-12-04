package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProvider
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.users.SUser

class DisableEmailOnBounce(private val sesIntegrationConfig: SESIntegrationConfig,
                           private val disabledEMailStateProvider: DisabledEMailStateProvider) : UserBounceHandler {
    override fun handleBounce(user: SUser) {
        if (sesIntegrationConfig.isDisableSendingMailOnBounce()) {
            disabledEMailStateProvider.disable(user, "Got bounce for email ${user.email}")
        }
    }
}