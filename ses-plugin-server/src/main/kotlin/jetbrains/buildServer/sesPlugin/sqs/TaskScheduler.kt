package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.serverSide.ServerListener
import jetbrains.buildServer.serverSide.ServerListenerAdapter
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.util.EventDispatcher
import java.util.concurrent.TimeUnit

class TaskScheduler(private val executorServices: ExecutorServices,
                    private val eventDispatcher: EventDispatcher<ServerListener>,
                    private val task: Runnable,
                    private val initialDelay: Long,
                    private val period: Long) : ServerListenerAdapter() {

    fun init() {
        eventDispatcher.addListener(this)
    }

    override fun serverStartup() {
        executorServices.normalExecutorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS)
    }
}