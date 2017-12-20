package jetbrains.buildServer.server.responsible

import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.testng.annotations.Test
import java.util.*

class ResponsibleContactProviderImplTest {
    @Test
    fun testGetGlobal() {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_MAIL_KEY); will(returnValue(Optional.of("someMail")))
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_DESCRIPTION_KEY); will(returnValue(Optional.of("some description")))
            }

            val global = ResponsibleContactProviderImpl(properties).getGlobal()
            then(global.isPresent).isTrue()
            then(global.get().email).isEqualTo("someMail")
            then(global.get().description).isEqualTo("some description")
        }
    }

    @Test
    fun testGetGlobalNoDescription() {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_MAIL_KEY); will(returnValue(Optional.of("someMail")))
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_DESCRIPTION_KEY); will(returnValue(Optional.empty<String>()))
            }

            val global = ResponsibleContactProviderImpl(properties).getGlobal()
            then(global.isPresent).isTrue()
            then(global.get().email).isEqualTo("someMail")
            then(global.get().description).isEqualTo("Contact someMail")
        }
    }

    @Test
    fun testGetGlobalNotConfigured() {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_MAIL_KEY); will(returnValue(Optional.empty<String>()))
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_DESCRIPTION_KEY); will(returnValue(Optional.empty<String>()))
            }

            val global = ResponsibleContactProviderImpl(properties).getGlobal()
            then(global.isPresent).isFalse()
        }
    }
}