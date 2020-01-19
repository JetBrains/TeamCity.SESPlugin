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
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.impl.UserImpl

class RemoveVerifiedStateOnBounce(private val sesIntegrationConfig: SESIntegrationConfig) : UserBounceHandler {
    companion object {
        val VERIFIED_EMAIL_PROPERTY_NAME_KEY = SimplePropertyKey(UserImpl.VERIFIED_EMAIL_PROPERTY_NAME)
    }

    override fun handleBounce(user: SUser) {
        if (sesIntegrationConfig.isDisableVerifiedMailOnBounce()) {
            user.deleteUserProperty(VERIFIED_EMAIL_PROPERTY_NAME_KEY)
        }
    }
}