package jetbrains.buildServer.server.responsible

import jetbrains.buildServer.serverSide.SBuildType
import jetbrains.buildServer.serverSide.SProject
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import java.util.*

class ResponsibleContactProviderImpl(private val properties: TeamCityProperties) : ResponsibleContactProvider {
    companion object {
        val TEAMCITY_PROPERTIES_MAIL_KEY = "teamcity.responsibleContact.email"
        val TEAMCITY_PROPERTIES_DESCRIPTION_KEY = "teamcity.responsibleContact.description"
    }

    override fun getGlobal(): Optional<ResponsibleContact> {
        val mail = properties.getString(TEAMCITY_PROPERTIES_MAIL_KEY)
        val description = properties.getString(TEAMCITY_PROPERTIES_DESCRIPTION_KEY)
        return if (mail.isPresent) {
            Optional.of(ResponsibleContact(mail.get(), description.orElse("Contact ${mail.get()}")))
        } else {
            Optional.empty()
        }
    }

    override fun get(project: SProject): Optional<ResponsibleContact> {
        return getGlobal()
    }

    override fun get(buildType: SBuildType): Optional<ResponsibleContact> {
        return getGlobal()
    }
}