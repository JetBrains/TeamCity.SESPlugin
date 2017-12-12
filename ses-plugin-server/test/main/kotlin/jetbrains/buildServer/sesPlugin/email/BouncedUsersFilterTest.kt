package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class BouncedUsersFilterTest {
    @Test
    fun test() {
        mocking {
            val disabledUserWithDescription = mock(SUser::class.java, "disabledUserWithDescription")
            val enabledUser = mock(SUser::class.java, "enabledUser")
            val provider = mock(DisabledEMailStateProvider::class.java)

            check {
                one(provider).isDisabled(disabledUserWithDescription); will(Expectations.returnValue(true))
                one(provider).getEmailDisableDescription(disabledUserWithDescription); will(Expectations.returnValue("description"))

                one(provider).isDisabled(enabledUser); will(Expectations.returnValue(false))
                never(provider).getEmailDisableDescription(enabledUser)
            }

            val filter = BouncedUsersFilter(provider)
            filter.test(disabledUserWithDescription)
            filter.test(enabledUser)
        }
    }
}