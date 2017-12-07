package jetbrains.buildServer.sesPlugin.teamcity.util

class NoOpLogService : LogService {
    override fun log(action: () -> Unit) {}
}