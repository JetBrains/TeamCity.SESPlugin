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

import jetbrains.buildServer.notification.email.EMailFilterResult
import jetbrains.buildServer.notification.email.EMailNotifierUserFilter
import jetbrains.buildServer.users.SUser

class BouncedUsersFilter(private val disabledEMailStateProvider: DisabledEMailStateProvider) : EMailNotifierUserFilter {
    override fun test(user: SUser) = if (disabledEMailStateProvider.isDisabled(user)) {
        EMailFilterResult(false, disabledEMailStateProvider.getEmailDisableDescription(user))
    } else {
        EMailFilterResult.OK
    }
}