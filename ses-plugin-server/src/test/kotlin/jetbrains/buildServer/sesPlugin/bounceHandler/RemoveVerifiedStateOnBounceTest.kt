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

import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class RemoveVerifiedStateOnBounceTest {
    @Test
    fun testHandleBounce() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)

            val user = mock(SUser::class)

            check {
                one(sesIntegrationConfig).isDisableVerifiedMailOnBounce(); will(Expectations.returnValue(true));
                one(user).deleteUserProperty(RemoveVerifiedStateOnBounce.VERIFIED_EMAIL_PROPERTY_NAME_KEY);
            }

            val removeVerifiedStateOnBounce = RemoveVerifiedStateOnBounce(sesIntegrationConfig)
            removeVerifiedStateOnBounce.handleBounce(user)
        }

    }

    @Test
    fun testHandleBounceDisabled() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)

            val user = mock(SUser::class)

            check {
                one(sesIntegrationConfig).isDisableVerifiedMailOnBounce(); will(Expectations.returnValue(false));
                never(user).deleteUserProperty(RemoveVerifiedStateOnBounce.VERIFIED_EMAIL_PROPERTY_NAME_KEY);
            }

            val removeVerifiedStateOnBounce = RemoveVerifiedStateOnBounce(sesIntegrationConfig)
            removeVerifiedStateOnBounce.handleBounce(user)
        }

    }
}