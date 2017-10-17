package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.serverSide.ProjectManager
import jetbrains.buildServer.serverSide.SProject

interface SESIntegrationManager {
    fun persistBean(bean: SESBean, project: SProject)

    fun getBeans(projectId: String): List<SESBean>
}

class SESIntegrationManagerImpl(private val myProjectManager: ProjectManager) : SESIntegrationManager {
    private val FEATURE_TYPE = "sesIntegration"

    @Synchronized
    override fun persistBean(bean: SESBean, project: SProject) {
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

    companion object {
        val ARN_PARAM = "aws.sesIntegration.sqsArn"
        val ACCOUNT_ID_PARAM = "aws.sesIntegration.accountId"
        val QUEUE_NAME_PARAM = "aws.sesIntegration.queueName"
    }
}