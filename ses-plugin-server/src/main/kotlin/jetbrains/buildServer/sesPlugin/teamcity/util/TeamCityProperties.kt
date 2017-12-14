package jetbrains.buildServer.sesPlugin.teamcity.util

interface TeamCityProperties {
    fun getLong(key: String, default: Long): Long
}