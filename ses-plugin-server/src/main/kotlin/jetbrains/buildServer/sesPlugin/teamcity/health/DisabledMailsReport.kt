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

package jetbrains.buildServer.sesPlugin.teamcity.health

import jetbrains.buildServer.serverSide.healthStatus.*
import jetbrains.buildServer.sesPlugin.teamcity.util.DisabledUsersProvider

class DisabledMailsReport(private val usersProvider: DisabledUsersProvider) : HealthStatusReport() {
    companion object {
        val id = "BlockedMailsReport"
        private val name = "Email address was disabled for some users"
        private val category = ItemCategory(id, name, ItemSeverity.INFO)
        private val displayText = "Email address was disabled for some users"
        val countKey = "count"
    }

    override fun report(scope: HealthStatusScope, consumer: HealthStatusItemConsumer) {
        consumer.consumeGlobal(HealthStatusItem(id, category, mapOf(countKey to usersProvider.count)))
    }

    override fun getType() = id

    override fun getDisplayName() = displayText

    override fun getCategories()
            = mutableListOf(category)

    override fun canReportItemsFor(scope: HealthStatusScope)
            = scope.globalItems() && scope.isItemWithSeverityAccepted(ItemSeverity.INFO)
}