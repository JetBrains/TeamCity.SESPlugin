package jetbrains.buildServer.sesPlugin.teamcity.util

class LogServiceImpl : LogService {
    override fun log(action: () -> Unit) = action.invoke()
}