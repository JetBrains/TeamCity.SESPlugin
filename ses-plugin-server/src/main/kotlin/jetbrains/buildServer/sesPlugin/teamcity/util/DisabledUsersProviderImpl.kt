package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel

class DisabledUsersProviderImpl(private val userModel: UserModel) : DisabledUsersProvider {

    override val count: Int
        get() {
            return users.count()
        }

    override val users: Set<SUser>
        get() {
            return userModel.findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false).users
        }
}