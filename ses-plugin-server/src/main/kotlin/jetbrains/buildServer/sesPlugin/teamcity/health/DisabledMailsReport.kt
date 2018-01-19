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