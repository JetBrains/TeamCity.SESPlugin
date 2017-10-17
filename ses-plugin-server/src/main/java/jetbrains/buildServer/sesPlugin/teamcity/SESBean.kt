package jetbrains.buildServer.sesPlugin.teamcity

interface SESBean {
    fun toMap(): Map<String, String>
}