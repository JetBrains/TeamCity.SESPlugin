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