package jetbrains.buildServer.sesPlugin.teamcity.util

interface PeriodicTask {
    val task: () -> Unit
    val initialDelay: Long
    val delay: Long
}