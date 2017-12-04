package jetbrains.buildServer.sesPlugin.bounceHandler

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.log.LogUtil
import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProvider
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.UserModel
import jetbrains.buildServer.users.impl.UserImpl

class BounceHandlerImpl(private val userModel: UserModel,
                        private val disabledEMailStateProvider: DisabledEMailStateProvider,
                        private val sesIntegrationConfig: SESIntegrationConfig) : BounceHandler {
    private val logger = Logger.getInstance(this::javaClass.name)

    override fun handleBounce(mail: String) {
        userModel.allUsers.users.filter {
            it.email == mail
        }.forEach {
            logger.warn("Got bounce for user ${LogUtil.describe(it)}")
            if (sesIntegrationConfig.isDisableSendingMailOnBounce()) {
                disabledEMailStateProvider.disable(it, "Got bounce for email $mail")
            }
            if (sesIntegrationConfig.isDisableVerifiedMailOnBounce()) {
                it.deleteUserProperty(SimplePropertyKey(UserImpl.VERIFIED_EMAIL_PROPERTY_NAME))
            }
        }
    }
}