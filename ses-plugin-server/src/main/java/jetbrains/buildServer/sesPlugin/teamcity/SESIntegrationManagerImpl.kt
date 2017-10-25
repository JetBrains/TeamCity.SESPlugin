package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.serverSide.ConfigActionFactory
import jetbrains.buildServer.serverSide.ProjectManager

class SESIntegrationManagerImpl(private val myProjectManager: ProjectManager,
                                private val myConfigActionFactory: ConfigActionFactory) : SESIntegrationManager {
    override fun createFrom(map: Map<String, String>): SESBean = SESBeanMapImpl(map)

    private val FEATURE_TYPE = "sesIntegration"

    @Synchronized
    override fun persistBean(bean: SESBean, projectId: String): PersistResult {
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            myProjectManager.rootProject.removeFeature(it.id)
        }

        myProjectManager.rootProject.addFeature(FEATURE_TYPE, bean.toMap())
        myProjectManager.rootProject.persist(myConfigActionFactory.createAction(myProjectManager.rootProject, "SES Integration config changed"))

        return PersistResult(true, "OK")
    }

    @Synchronized
    override fun getBeans(projectId: String): List<SESBean> {
        val f = myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE)

        return f.asSequence().map {
            SESBeanMapImpl(it.parameters)
        }.toList()
    }
}