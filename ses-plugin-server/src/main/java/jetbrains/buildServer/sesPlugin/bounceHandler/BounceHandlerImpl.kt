package jetbrains.buildServer.sesPlugin.bounceHandler

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.log.LogUtil
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService
import jetbrains.buildServer.sesPlugin.teamcity.util.UserSetProvider

class BounceHandlerImpl(private val userProvider: UserSetProvider,
                        private val userHandlers: Collection<UserBounceHandler>,
                        private val loggerService: LogService = NoOpLogService()) : BounceHandler {
    private val logger = Logger.getInstance(this::javaClass.name)

    override fun handleBounce(mail: String) {
        userProvider.users.filter {
            it.email == mail
        }.forEach {
            loggerService.log {
                logger.warn("Got bounce for user ${LogUtil.describe(it)}")
            }

            userHandlers.forEach { handler -> handler.handleBounce(it) }
        }
    }
}