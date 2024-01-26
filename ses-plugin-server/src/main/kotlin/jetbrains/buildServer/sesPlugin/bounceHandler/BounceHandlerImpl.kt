

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.util.UserSetProvider

class BounceHandlerImpl(private val userProvider: UserSetProvider,
                        private val userHandlers: Collection<UserBounceHandler>) : BounceHandler {

    override fun handleBounces(mails: Sequence<String>) {
        val set = mails.toHashSet()

        userProvider.users.filter {
            set.contains(it.email)
        }.forEach {
            userHandlers.forEach { handler -> handler.handleBounce(it) }
        }
    }
}