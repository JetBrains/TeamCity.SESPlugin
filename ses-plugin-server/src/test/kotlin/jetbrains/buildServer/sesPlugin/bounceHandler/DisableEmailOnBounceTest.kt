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

package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProvider
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationConfig
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class DisableEmailOnBounceTest {

    @Test
    fun testHandleBounce() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)
            val disabledEMailStateProvider = mock(DisabledEMailStateProvider::class)

            val user = mock(SUser::class)
            val description = "Got bounce for email someMail"

            check {
                one(sesIntegrationConfig).isDisableSendingMailOnBounce(); will(Expectations.returnValue(true));
                one(user).email; will(Expectations.returnValue("someMail"));
                one(disabledEMailStateProvider).disable(user, description);
            }

            val disableEmailOnBounce = DisableEmailOnBounce(sesIntegrationConfig, disabledEMailStateProvider)
            disableEmailOnBounce.handleBounce(user)
        }
    }

    @Test
    fun testHandleBounceDisabled() {
        mocking {
            val sesIntegrationConfig = mock(SESIntegrationConfig::class)
            val disabledEMailStateProvider = mock(DisabledEMailStateProvider::class)

            val user = mock(SUser::class)
            val description = "Got bounce for email someMail"

            check {
                one(sesIntegrationConfig).isDisableSendingMailOnBounce(); will(Expectations.returnValue(false));
                never(disabledEMailStateProvider).disable(user, description);
            }

            val disableEmailOnBounce = DisableEmailOnBounce(sesIntegrationConfig, disabledEMailStateProvider)
            disableEmailOnBounce.handleBounce(user)
        }
    }
}