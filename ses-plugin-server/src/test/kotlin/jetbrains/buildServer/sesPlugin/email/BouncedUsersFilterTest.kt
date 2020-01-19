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

package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class BouncedUsersFilterTest {
    @Test
    fun test() {
        mocking {
            val disabledUserWithDescription = mock(SUser::class, "disabledUserWithDescription")
            val enabledUser = mock(SUser::class, "enabledUser")
            val provider = mock(DisabledEMailStateProvider::class)

            check {
                one(provider).isDisabled(disabledUserWithDescription); will(Expectations.returnValue(true))
                one(provider).getEmailDisableDescription(disabledUserWithDescription); will(Expectations.returnValue("description"))

                one(provider).isDisabled(enabledUser); will(Expectations.returnValue(false))
                never(provider).getEmailDisableDescription(enabledUser)
            }

            val filter = BouncedUsersFilter(provider)
            filter.test(disabledUserWithDescription)
            filter.test(enabledUser)
        }
    }
}