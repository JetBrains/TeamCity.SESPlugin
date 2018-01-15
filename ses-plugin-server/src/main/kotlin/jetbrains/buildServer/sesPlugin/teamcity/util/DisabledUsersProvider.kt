package jetbrains.buildServer.sesPlugin.teamcity.util

interface DisabledUsersProvider : UserSetProvider {
    val count: Int
}