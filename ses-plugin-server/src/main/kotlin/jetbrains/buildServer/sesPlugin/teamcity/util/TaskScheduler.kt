package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.serverSide.ServerListener
import jetbrains.buildServer.serverSide.ServerListenerAdapter
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.util.EventDispatcher
import java.util.concurrent.TimeUnit

class TaskScheduler(private val executorServices: ExecutorServices,
                    private val eventDispatcher: EventDispatcher<ServerListener>,
                    private val tasks: Collection<PeriodicTask>) : ServerListenerAdapter() {

    fun init() {
        eventDispatcher.addListener(this)
    }

    override fun serverStartup() {
        val normalExecutorService = executorServices.normalExecutorService
        for (task in tasks) {
            normalExecutorService.scheduleAtFixedRate(task.task, task.initialDelay, task.delay, TimeUnit.MILLISECONDS)
        }
    }
}