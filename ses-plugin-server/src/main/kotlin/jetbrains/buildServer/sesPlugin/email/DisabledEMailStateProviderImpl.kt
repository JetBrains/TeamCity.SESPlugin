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

package jetbrains.buildServer.sesPlugin.email

import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.users.SimplePropertyKey
import jetbrains.buildServer.users.impl.UserImpl

class DisabledEMailStateProviderImpl : DisabledEMailStateProvider {

    companion object {
        val DISABLED_PROPERTY_KEY = SimplePropertyKey("${UserImpl.PROTECTED_USER_PROPERTY_PREFIX}email.disabled")
        val DISABLE_DESCRIPTION_PROPERTY_KEY = SimplePropertyKey("${UserImpl.PROTECTED_USER_PROPERTY_PREFIX}email.disableDescription")
    }

    override fun disable(user: SUser, description: String) {
        setDisabled(user, true)
        setEmailDisableDescription(user, description)
    }

    override fun setDisabled(user: SUser, state: Boolean) {
        user.emailDisabled = state
    }

    override fun isDisabled(user: SUser): Boolean = user.emailDisabled

    override fun setEmailDisableDescription(user: SUser, description: String) {
        user.emailDisableDescription = description
    }

    override fun removeEmailDisableDescription(user: SUser) {
        user.emailDisableDescription = ""
    }

    override fun getEmailDisableDescription(user: SUser) = user.emailDisableDescription
}

var SUser.emailDisabled: Boolean
    set(state) = if (state) {
        setUserProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY, state.toString())
    } else {
        deleteUserProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY)
        deleteUserProperty(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY)
    }
    get() = getBooleanProperty(DisabledEMailStateProviderImpl.DISABLED_PROPERTY_KEY)

var SUser.emailDisableDescription: String
    set(description) = if (!description.isEmpty()) {
        setUserProperty(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY, description)
    } else {
        deleteUserProperty(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY)
    }
    get() = getPropertyValue(DisabledEMailStateProviderImpl.DISABLE_DESCRIPTION_PROPERTY_KEY) ?: if (emailDisabled) "disabled" else "enabled"