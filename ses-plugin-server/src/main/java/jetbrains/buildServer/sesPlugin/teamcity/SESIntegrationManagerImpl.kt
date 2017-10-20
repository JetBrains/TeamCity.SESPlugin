package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.serverSide.ProjectManager

class SESIntegrationManagerImpl(private val myProjectManager: ProjectManager) : SESIntegrationManager {
    override fun createFrom(map: Map<String, String>): SESBean = SESBeanMapImpl(map)

    private val FEATURE_TYPE = "sesIntegration"

    @Synchronized
    override fun persistBean(bean: SESBean, projectId: String) {
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            myProjectManager.rootProject.removeFeature(it.id)
        }
        myProjectManager.rootProject.addFeature(FEATURE_TYPE, bean.toMap())
    }

    @Synchronized
    override fun getBeans(projectId: String): List<SESBean> {
        val f = myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE)

        return f.asSequence().map {
            SESBeanMapImpl(it.parameters)
        }.toList()
    }
}