package jetbrains.buildServer.sesPlugin.bounceHandler

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.log.LogUtil
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel

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

    /**
     * Service interface to wrap UserModel
     */
    interface UserSetProvider {
        val users: Set<SUser>
    }

    class AllUsersProvider(private val userModel: UserModel) : BounceHandlerImpl.UserSetProvider {
        override val users: Set<SUser> get() = userModel.allUsers.users
    }
}