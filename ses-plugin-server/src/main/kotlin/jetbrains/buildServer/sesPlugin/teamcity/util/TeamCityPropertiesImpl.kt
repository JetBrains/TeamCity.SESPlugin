package jetbrains.buildServer.sesPlugin.teamcity.util

class TeamCityPropertiesImpl : TeamCityProperties {
    override fun getLong(key: String, default: Long): Long {
        return jetbrains.buildServer.serverSide.TeamCityProperties.getLong(key, default)
    }
}