package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.serverSide.ServerListener
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.util.EventDispatcher
import org.jmock.Expectations
import org.testng.annotations.Test
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TaskSchedulerTest {
    @Test
    fun testNothingDoneBeforeServerStartup() {
        mocking {
            val executorServices = mock(ExecutorServices::class)
            val task = mock(Runnable::class)
            val dispatcher = EventDispatcher.create(ServerListener::class.java)

            val startupProcessor = TaskScheduler(executorServices, dispatcher, task, 10, 11)

            check {
                never(executorServices).normalExecutorService
            }

            startupProcessor.init()
        }
    }


    @Test
    fun test() {
        mocking {
            val executorServices = mock(ExecutorServices::class)
            val executor = mock(ScheduledExecutorService::class)
            val task = mock(Runnable::class)
            val dispatcher = EventDispatcher.create(ServerListener::class.java)

            val startupProcessor = TaskScheduler(executorServices, dispatcher, task, 10, 11)

            check {
                one(executorServices).normalExecutorService; will(Expectations.returnValue(executor))
                one(executor).scheduleAtFixedRate(task, 10, 11, TimeUnit.MILLISECONDS)
            }

            startupProcessor.init()
            dispatcher.multicaster.serverStartup()
        }
    }
}