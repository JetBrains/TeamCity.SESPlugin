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

import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations.returnValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class ConfigurableLongImplTest {
    @Test(dataProvider = "data")
    fun test(prop: Long, default: Long, min: Long, result: Long) {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getLong("key", default); will(returnValue(if (prop >= 0) prop else default))
            }

            val longImpl = ConfigurableLongImpl(properties, "key", default, min)
            BDDAssertions.then(longImpl.get()).describedAs("prop = [$prop], default = [$default], min = [$min], result = [$result]").isEqualTo(result)
        }
    }

    @DataProvider(name = "data")
    private fun data(): Array<Array<Long>> {
        return arrayOf(
                arrayOf(2L, 10L, 1L, 2L),
                arrayOf(2L, 10L, 3L, 3L),
                arrayOf(-1L, 2L, 1L, 2L),
                arrayOf(1L, 2L, 2L, 2L)
        )
    }
}