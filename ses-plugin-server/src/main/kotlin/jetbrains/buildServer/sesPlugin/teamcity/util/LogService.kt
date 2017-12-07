package jetbrains.buildServer.sesPlugin.teamcity.util

/**
 * Service interface to safely mock classes using logging
 */
interface LogService {
    fun log(action: () -> Unit)
}