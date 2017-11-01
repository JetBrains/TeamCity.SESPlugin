package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.serverSide.ConfigActionFactory
import jetbrains.buildServer.serverSide.ProjectManager

class SESIntegrationManagerImpl(private val myProjectManager: ProjectManager,
                                private val myConfigActionFactory: ConfigActionFactory) : SESIntegrationManager {
    override fun createFrom(map: Map<String, String>): SQSBean = SQSBeanMapImpl(map)

    private val FEATURE_TYPE = "sesIntegration"

    @Synchronized
    override fun persistBean(bean: SQSBean, projectId: String): PersistResult {
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            myProjectManager.rootProject.removeFeature(it.id)
        }

        myProjectManager.rootProject.addFeature(FEATURE_TYPE, bean.toMap())
        myProjectManager.rootProject.persist(myConfigActionFactory.createAction(myProjectManager.rootProject, "SES Integration config changed"))

        return PersistResult(true, "OK")
    }

    override fun deleteBean(projectId: String): PersistResult {
        var deleted = false
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            myProjectManager.rootProject.removeFeature(it.id)
            deleted = true
        }

        if (deleted) {
            myProjectManager.rootProject.persist(myConfigActionFactory.createAction(myProjectManager.rootProject, "SES Integration config removed"))
        }

        return if (deleted) PersistResult(true, "OK") else PersistResult(false, "No SES integration configured")
    }

    @Synchronized
    override fun getBeans(projectId: String): List<SQSBean> {
        val f = myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE)

        return f.asSequence().map {
            SQSBeanMapImpl(it.parameters)
        }.toList()
    }
}