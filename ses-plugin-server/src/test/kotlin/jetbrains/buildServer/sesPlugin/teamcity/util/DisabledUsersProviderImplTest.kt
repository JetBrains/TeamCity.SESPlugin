package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel
import jetbrains.buildServer.users.UserSet
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.testng.annotations.Test

class DisabledUsersProviderImplTest {
    @Test
    fun testUsers() {
        mocking {
            val userModel = mock(UserModel::class)
            check {
                one(userModel).findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false)
            }

            val provider = DisabledUsersProviderImpl(userModel)
            provider.users
        }
    }

    @Test
    fun testCount() {
        mocking {
            val userModel = mock(UserModel::class)
            val usersSet = mock(UserSet::class)
            check {
                one(userModel).findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false); will(Expectations.returnValue(usersSet))
                one(usersSet).users; will(Expectations.returnValue(setOf(mock(SUser::class, "user1"), mock(SUser::class, "user2"))))
            }

            val provider = DisabledUsersProviderImpl(userModel)
            then(provider.count).isEqualTo(2)
        }
    }
}