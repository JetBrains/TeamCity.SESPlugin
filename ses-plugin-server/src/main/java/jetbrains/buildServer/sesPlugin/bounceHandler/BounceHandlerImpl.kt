package jetbrains.buildServer.sesPlugin.bounceHandler

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.log.LogUtil
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.UserModel
import jetbrains.buildServer.users.impl.UserImpl

class BounceHandlerImpl(private val userModel: UserModel) : BounceHandler {
    private val logger = Logger.getInstance(this::javaClass.name)

    override fun handleBounce(mail: String) {
        userModel.allUsers.users.filter {
            it.email == mail
        }.forEach {
            logger.warn("Got bounce for user ${LogUtil.describe(it)}")
            it.deleteUserProperty(SimplePropertyKey(UserImpl.VERIFIED_EMAIL_PROPERTY_NAME))
        }
    }
}