

package jetbrains.buildServer.server.responsible

import jetbrains.buildServer.serverSide.SBuildType
import jetbrains.buildServer.serverSide.SProject
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import java.util.*

class ResponsibleContactProviderImpl(private val properties: TeamCityProperties) : ResponsibleContactProvider {
    companion object {
        val TEAMCITY_PROPERTIES_DESCRIPTION_KEY = "teamcity.responsibleContact.description"
    }

    override fun getGlobal(): Optional<String> {
        val description = properties.getString(TEAMCITY_PROPERTIES_DESCRIPTION_KEY)
        return if (description.isPresent) {
            Optional.of(description.get())
        } else {
            Optional.empty()
        }
    }

    override fun get(project: SProject): Optional<String> {
        return getGlobal()
    }

    override fun get(buildType: SBuildType): Optional<String> {
        return getGlobal()
    }
}