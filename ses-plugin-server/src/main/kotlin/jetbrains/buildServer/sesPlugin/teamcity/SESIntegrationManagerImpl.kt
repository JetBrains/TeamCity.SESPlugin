/*
 * Copyright 2000-2020 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.serverSide.ConfigActionFactory
import jetbrains.buildServer.serverSide.ProjectManager
import jetbrains.buildServer.sesPlugin.data.PersistResult
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants

class SESIntegrationManagerImpl(private val myProjectManager: ProjectManager,
                                private val myConfigActionFactory: ConfigActionFactory) : SESIntegrationManager {
    override fun createFrom(map: Map<String, String>): SQSBean = SQSBeanMapImpl(map)

    companion object {
        private val FEATURE_TYPE = "sesIntegration"
    }

    @Synchronized
    override fun persistBean(bean: SQSBean, projectId: String): PersistResult {
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            myProjectManager.rootProject.removeFeature(it.id)
        }

        myProjectManager.rootProject.addFeature(FEATURE_TYPE, bean.toMap())
        myProjectManager.rootProject.persist(myConfigActionFactory.createAction(myProjectManager.rootProject, "SES Integration config changed"))

        return PersistResult(true, "OK")
    }

    @Synchronized
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
    override fun getBeans(projectId: String): Sequence<SQSBean> {
        val f = myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE)

        return f.asSequence().map {
            SQSBeanMapImpl(it.parameters)
        }
    }

    @Synchronized
    override fun setEnabled(projectId: String, state: Boolean) {
        myProjectManager.rootProject.getOwnFeaturesOfType(FEATURE_TYPE).forEach {
            val parameters = HashMap(it.parameters)

            parameters[Constants.ENABLED] = state.toString()
            myProjectManager.rootProject.updateFeature(it.id, FEATURE_TYPE, parameters)
        }

    }
}