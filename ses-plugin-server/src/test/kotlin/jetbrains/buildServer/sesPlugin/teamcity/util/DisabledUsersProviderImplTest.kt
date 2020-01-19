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

package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel
import jetbrains.buildServer.users.UserSet
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations
import org.testng.annotations.Test

class DisabledUsersProviderImplTest {
    @Test
    fun testUsers() {
        mocking {
            val userModel = mock(UserModel::class)
            check {
                one(userModel).findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false)
            }

            val provider = DisabledUsersProviderImpl(userModel)
            provider.users
        }
    }

    @Test
    fun testCount() {
        mocking {
            val userModel = mock(UserModel::class)
            val usersSet = mock(UserSet::class)
            check {
                one(userModel).findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false); will(Expectations.returnValue(usersSet))
                one(usersSet).users; will(Expectations.returnValue(setOf(mock(SUser::class, "user1"), mock(SUser::class, "user2"))))
            }

            val provider = DisabledUsersProviderImpl(userModel)
            then(provider.count).isEqualTo(2)
        }
    }
}