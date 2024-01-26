

package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.users.SUser

/**
 * Service interface to wrap UserModel
 */
interface UserSetProvider {
    val users: Set<SUser>
}