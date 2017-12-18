package jetbrains.buildServer.sesPlugin.teamcity.util

class TeamCityPropertiesImpl : TeamCityProperties {
    override fun getBoolean(key: String, default: Boolean): Boolean {
        return if (default) jetbrains.buildServer.serverSide.TeamCityProperties.getBooleanOrTrue(key) else jetbrains.buildServer.serverSide.TeamCityProperties.getBoolean(key)
    }

    override fun getLong(key: String, default: Long): Long {
        return jetbrains.buildServer.serverSide.TeamCityProperties.getLong(key, default)
    }
}