package jetbrains.buildServer.sesPlugin.teamcity

class SQSBeanMapImpl(private val data: Map<String, String>) : SQSBean {
    override fun toMap(): Map<String, String> {
        return data
    }
}