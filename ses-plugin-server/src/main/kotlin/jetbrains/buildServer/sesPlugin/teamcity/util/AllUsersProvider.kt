

package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel

class AllUsersProvider(private val userModel: UserModel) : UserSetProvider {
    override val users: Set<SUser> get() = userModel.allUsers.users
}