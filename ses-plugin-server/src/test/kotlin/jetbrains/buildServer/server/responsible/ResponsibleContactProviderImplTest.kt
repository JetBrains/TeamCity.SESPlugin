/*
 * Copyright 2000-2020 JetBrains s.r.o.
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

import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.testng.annotations.Test
import java.util.*

class ResponsibleContactProviderImplTest {
    @Test
    fun testGetGlobal() {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_DESCRIPTION_KEY); will(returnValue(Optional.of("some description")))
            }

            val global = ResponsibleContactProviderImpl(properties).getGlobal()
            then(global.isPresent).isTrue()
            then(global.get()).isEqualTo("some description")
        }
    }

    @Test
    fun testGetGlobalNotConfigured() {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getString(ResponsibleContactProviderImpl.TEAMCITY_PROPERTIES_DESCRIPTION_KEY); will(returnValue(Optional.empty<String>()))
            }

            val global = ResponsibleContactProviderImpl(properties).getGlobal()
            then(global.isPresent).isFalse()
        }
    }
}