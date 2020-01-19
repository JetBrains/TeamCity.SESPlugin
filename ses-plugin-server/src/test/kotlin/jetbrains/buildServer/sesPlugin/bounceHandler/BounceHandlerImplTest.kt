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

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.util.UserSetProvider
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class BounceHandlerImplTest {

    @Test
    fun testBulkHandle() {
        mocking {
            val userBounceHandler = mock(UserBounceHandler::class)
            val userSetProvider = mock(UserSetProvider::class)
            val user1 = mock(SUser::class, "user1")
            val user2 = mock(SUser::class, "user2")

            val handler = BounceHandlerImpl(userSetProvider, listOf(userBounceHandler))

            val mail1 = "mail1"
            val mail2 = "mail2"

            check {
                one(userSetProvider).users; will(Expectations.returnValue(setOf(user1, user2)))
                one(user2).email; will(Expectations.returnValue("mail1"))
                one(user1).email; will(Expectations.returnValue("mail2"))
                one(userBounceHandler).handleBounce(user1)
                one(userBounceHandler).handleBounce(user2)
            }

            handler.handleBounces(sequenceOf(mail1, mail2))
        }
    }
}