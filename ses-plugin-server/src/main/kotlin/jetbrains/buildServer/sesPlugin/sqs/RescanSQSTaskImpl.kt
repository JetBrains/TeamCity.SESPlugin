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