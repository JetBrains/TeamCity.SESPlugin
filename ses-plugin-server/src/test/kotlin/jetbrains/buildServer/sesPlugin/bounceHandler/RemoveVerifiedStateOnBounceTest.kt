

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class RemoveVerifiedStateOnBounceTest {
    @Test
    fun testHandleBounce() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)

            val user = mock(SUser::class)

            check {
                one(sesIntegrationConfig).isDisableVerifiedMailOnBounce(); will(Expectations.returnValue(true));
                one(user).deleteUserProperty(RemoveVerifiedStateOnBounce.VERIFIED_EMAIL_PROPERTY_NAME_KEY);
            }

            val removeVerifiedStateOnBounce = RemoveVerifiedStateOnBounce(sesIntegrationConfig)
            removeVerifiedStateOnBounce.handleBounce(user)
        }

    }

    @Test
    fun testHandleBounceDisabled() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)

            val user = mock(SUser::class)

            check {
                one(sesIntegrationConfig).isDisableVerifiedMailOnBounce(); will(Expectations.returnValue(false));
                never(user).deleteUserProperty(RemoveVerifiedStateOnBounce.VERIFIED_EMAIL_PROPERTY_NAME_KEY);
            }

            val removeVerifiedStateOnBounce = RemoveVerifiedStateOnBounce(sesIntegrationConfig)
            removeVerifiedStateOnBounce.handleBounce(user)
        }

    }
}