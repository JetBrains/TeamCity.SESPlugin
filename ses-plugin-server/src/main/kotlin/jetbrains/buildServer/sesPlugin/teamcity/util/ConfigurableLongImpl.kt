package jetbrains.buildServer.sesPlugin.teamcity.util

class ConfigurableLongImpl(private val properties: TeamCityProperties, private val key: String, private val default: Long, private val min: Long) : ConfigurableLong {
    override fun get(): Long {
        return Math.max(min, properties.getLong(key, default))
    }
}