package jetbrains.buildServer.sesPlugin.teamcity

interface SQSBean {
    fun toMap(): Map<String, String>
}