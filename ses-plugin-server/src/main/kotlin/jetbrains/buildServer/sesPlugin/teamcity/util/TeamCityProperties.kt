package jetbrains.buildServer.sesPlugin.teamcity.util

interface TeamCityProperties {
    fun getLong(key: String, default: Long): Long
    fun getBoolean(key: String, default: Boolean): Boolean
}