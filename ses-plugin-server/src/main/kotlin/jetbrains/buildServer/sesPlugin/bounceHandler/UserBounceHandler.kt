

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.users.SUser

/**
 * Interface to handle bounce events occurred for a user
 */
interface UserBounceHandler {
    fun handleBounce(user: SUser)
}