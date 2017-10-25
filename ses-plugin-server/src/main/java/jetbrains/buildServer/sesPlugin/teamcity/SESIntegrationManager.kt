package jetbrains.buildServer.sesPlugin.teamcity

interface SESIntegrationManager {
    fun persistBean(bean: SESBean, projectId: String): PersistResult

    fun getBeans(projectId: String): List<SESBean>

    fun createFrom(map: Map<String, String>): SESBean
}
