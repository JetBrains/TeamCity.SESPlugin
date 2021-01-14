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

import jetbrains.buildServer.sesPlugin.email.DisabledEMailStateProviderImpl
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.UserModel

class DisabledUsersProviderImpl(private val userModel: UserModel) : DisabledUsersProvider {

    override val count: Int
        get() {
            return users.count()
        }

    override val users: Set<SUser>
        get() {
            return userModel.findUsersByPropertyValue(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, true.toString(), false).users
        }
}