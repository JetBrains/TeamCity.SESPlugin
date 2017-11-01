package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.serverSide.ServerListenerAdapter
import jetbrains.buildServer.serverSide.ServerListenerEventDispatcher
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import java.util.concurrent.TimeUnit

class StartupProcessor(private val executorServices: ExecutorServices,
                       private val queueReader: SQSMessagesReader,
                       private val sesIntegrationManager: SESIntegrationManager,
                       private val eventDispatcher: ServerListenerEventDispatcher) : ServerListenerAdapter() {

    fun init() {
        eventDispatcher.addListener(this)
    }

    private val initialDelay = TimeUnit.MINUTES.toMillis(10L)

    private val period = TimeUnit.MINUTES.toMillis(10L)

    override fun serverStartup() {
        executorServices.normalExecutorService.scheduleAtFixedRate({
            queueReader.readAllQueues(sesIntegrationManager.getBeans("").asSequence())
        }, initialDelay, period, TimeUnit.MILLISECONDS)
    }
}