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

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import jetbrains.buildServer.sesPlugin.teamcity.util.*
import java.util.concurrent.TimeUnit

class RescanSQSTaskImpl(private val queueReader: SQSMessagesReader,
                        private val sesIntegrationManager: SESIntegrationManager,
                        private val _delay: ConfigurableLong,
                        private val _initialDelay: ConfigurableLong,
                        private val logService: LogService = NoOpLogService()) : PeriodicTask {
    override val task: () -> Unit
        get() = {
            logService.log {
                Loggers.SERVER.debug("Amazon SES plugin: reading all queues")
            }
            queueReader.readAllQueues(sesIntegrationManager.getBeans(""))
        }

    override val initialDelay: Long
        get() = _initialDelay.get()

    override val delay: Long
        get() = _delay.get()
}

class RescanSQSTaskDelay(properties: TeamCityProperties) :
        ConfigurableLong by ConfigurableLongImpl(properties, "teamcity.sesPlugin.sqsReadPeriod", TimeUnit.MINUTES.toMillis(10), TimeUnit.MINUTES.toMillis(1))

class RescanSQSTaskInitialDelay(properties: TeamCityProperties) :
        ConfigurableLong by ConfigurableLongImpl(properties, "teamcity.sesPlugin.sqsReadInitialDelay", TimeUnit.MINUTES.toMillis(10), TimeUnit.MINUTES.toMillis(1))