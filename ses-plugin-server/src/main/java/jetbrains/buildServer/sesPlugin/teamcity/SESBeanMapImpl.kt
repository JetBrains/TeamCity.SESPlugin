package jetbrains.buildServer.sesPlugin.teamcity

class SESBeanMapImpl(private val data: Map<String, String>) : SESBean {
    override fun toMap(): Map<String, String> {
        return data
    }
}