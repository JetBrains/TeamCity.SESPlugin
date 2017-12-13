package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProvider
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class DisableEmailOnBounceTest {

    @Test
    fun testHandleBounce() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)
            val disabledEMailStateProvider = mock(DisabledEMailStateProvider::class)

            val user = mock(SUser::class)
            val description = "Got bounce for email someMail"

            check {
                one(sesIntegrationConfig).isDisableSendingMailOnBounce(); will(Expectations.returnValue(true));
                one(user).email; will(Expectations.returnValue("someMail"));
                one(disabledEMailStateProvider).disable(user, description);
            }

            val disableEmailOnBounce = DisableEmailOnBounce(sesIntegrationConfig, disabledEMailStateProvider)
            disableEmailOnBounce.handleBounce(user)
        }
    }

    @Test
    fun testHandleBounceDisabled() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)
            val disabledEMailStateProvider = mock(DisabledEMailStateProvider::class)

            val user = mock(SUser::class)
            val description = "Got bounce for email someMail"

            check {
                one(sesIntegrationConfig).isDisableSendingMailOnBounce(); will(Expectations.returnValue(false));
                never(disabledEMailStateProvider).disable(user, description);
            }

            val disableEmailOnBounce = DisableEmailOnBounce(sesIntegrationConfig, disabledEMailStateProvider)
            disableEmailOnBounce.handleBounce(user)
        }
    }
}