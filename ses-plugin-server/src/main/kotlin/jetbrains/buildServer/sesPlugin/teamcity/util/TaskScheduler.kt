package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.ServerListener
import jetbrains.buildServer.serverSide.ServerListenerAdapter
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.util.EventDispatcher
import java.util.concurrent.TimeUnit

class TaskScheduler(private val executorServices: ExecutorServices,
                    private val eventDispatcher: EventDispatcher<ServerListener>,
                    private val tasks: Collection<PeriodicTask>,
                    private val logService: LogService = NoOpLogService()) : ServerListenerAdapter() {

    fun init() {
        eventDispatcher.addListener(this)
    }

    override fun serverStartup() {
        val normalExecutorService = executorServices.normalExecutorService
        for (task in tasks) {
            normalExecutorService.scheduleAtFixedRate({
                try {
                    task.task
                } catch (ex: Throwable) {
                    logService.log {
                        Loggers.SERVER.warn("Error executing scheduled task in TeamCity SES plugin", ex)
                    }
                }
            }, task.initialDelay, task.delay, TimeUnit.MILLISECONDS)
        }
    }
}