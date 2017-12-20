package jetbrains.buildServer.sesPlugin.teamcity.util

import java.util.*

interface TeamCityProperties {
    fun getLong(key: String, default: Long): Long
    fun getLong(key: String): Optional<Long>

    fun getBoolean(key: String, default: Boolean): Boolean
    fun getBoolean(key: String): Optional<Boolean>

    fun getString(key: String, default: String): String
    fun getString(key: String): Optional<String>
}