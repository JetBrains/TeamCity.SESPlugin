package jetbrains.buildServer.sesPlugin.teamcity.util

import java.util.*

typealias StaticProperties = jetbrains.buildServer.serverSide.TeamCityProperties

class TeamCityPropertiesImpl : TeamCityProperties {
    override fun getBoolean(key: String, default: Boolean): Boolean {
        return if (default) StaticProperties.getBooleanOrTrue(key) else StaticProperties.getBoolean(key)
    }

    override fun getBoolean(key: String): Optional<Boolean> {
        val prop = StaticProperties.getPropertyOrNull(key)
        return if (prop == null) {
            Optional.empty()
        } else {
            Optional.of(prop.trim().toBoolean())
        }
    }

    override fun getLong(key: String, default: Long): Long {
        return StaticProperties.getLong(key, default)
    }

    override fun getLong(key: String): Optional<Long> {
        val prop = StaticProperties.getPropertyOrNull(key)
        return if (prop == null) {
            Optional.empty()
        } else {
            Optional.of(prop.trim().toLong())
        }
    }

    override fun getString(key: String, default: String): String {
        return StaticProperties.getProperty(key, default)
    }

    override fun getString(key: String): Optional<String> {
        return Optional.ofNullable(StaticProperties.getPropertyOrNull(key))
    }
}