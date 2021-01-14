/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.sesPlugin.teamcity.util

import java.util.*

typealias StaticProperties = jetbrains.buildServer.serverSide.TeamCityProperties

class TeamCityPropertiesImpl : TeamCityProperties {
    override fun getInt(key: String, default: Int): Int {
        return StaticProperties.getInteger(key, default)
    }

    override fun getInt(key: String): Optional<Int> {
        return StaticProperties.getPropertyOrNull(key)?.let { Optional.of(it.trim().toInt()) } ?: Optional.empty()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return if (default) StaticProperties.getBooleanOrTrue(key) else StaticProperties.getBoolean(key)
    }

    override fun getBoolean(key: String): Optional<Boolean> {
        return StaticProperties.getPropertyOrNull(key)?.let { Optional.of(it.trim().toBoolean()) } ?: Optional.empty()
    }

    override fun getLong(key: String, default: Long): Long {
        return StaticProperties.getLong(key, default)
    }

    override fun getLong(key: String): Optional<Long> {
        return StaticProperties.getPropertyOrNull(key)?.let { Optional.of(it.trim().toLong()) } ?: Optional.empty()
    }

    override fun getString(key: String, default: String): String {
        return StaticProperties.getProperty(key, default)
    }

    override fun getString(key: String): Optional<String> {
        return Optional.ofNullable(StaticProperties.getPropertyOrNull(key))
    }
}