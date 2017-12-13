package jetbrains.buildServer.sesPlugin.teamcity

interface SESIntegrationManager {
    fun persistBean(bean: SQSBean, projectId: String): PersistResult

    fun deleteBean(projectId: String): PersistResult

    fun getBeans(projectId: String): Sequence<SQSBean>

    fun createFrom(map: Map<String, String>): SQSBean
}
