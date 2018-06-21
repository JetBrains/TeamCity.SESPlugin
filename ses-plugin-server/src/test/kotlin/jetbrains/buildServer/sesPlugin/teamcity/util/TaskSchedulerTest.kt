package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.serverSide.ServerListener
import jetbrains.buildServer.serverSide.executors.ExecutorServices
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.util.EventDispatcher
import org.jmock.Expectations.*
import org.jmock.api.Invocation
import org.jmock.lib.action.CustomAction
import org.testng.annotations.Test
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class TaskSchedulerTest {
    @Test
    fun testNothingDoneBeforeServerStartup() {
        mocking {
            val executorServices = mock(ExecutorServices::class)
            val task = mock(PeriodicTask::class)
            val dispatcher = EventDispatcher.create(ServerListener::class.java)

            val startupProcessor = TaskScheduler(executorServices, dispatcher, listOf(task))

            check {
                never(executorServices)
                never(task)
            }

            startupProcessor.init()
        }
    }

    @Test
    fun test() {
        mocking {
            val executorServices = mock(ExecutorServices::class)
            val executor = mock(ScheduledExecutorService::class)
            val task = mock(PeriodicTask::class)
            val taskRunnable: () -> Unit = {}
            val dispatcher = EventDispatcher.create(ServerListener::class.java)
            val future = mock(ScheduledFuture::class)

            val startupProcessor = TaskScheduler(executorServices, dispatcher, listOf(task))

            check {
                one(executorServices).normalExecutorService; will(returnValue(executor))

                one(task).task; will(returnValue(taskRunnable))
                one(task).initialDelay; will(returnValue(10L))
                one(task).delay; will(returnValue(11L))

                one(executor).scheduleAtFixedRate(with(any(Runnable::class.java)), with(10L), with(11L), with(TimeUnit.MILLISECONDS));
                will(object : CustomAction("") {
                    override fun invoke(p0: Invocation?): Any {
                        (p0!!.getParameter(0) as Runnable).run()
                        return future
                    }

                })
            }

            startupProcessor.init()
            dispatcher.multicaster.serverStartup()
        }
    }

    @Test
    fun testErrorOccurred() {
        mocking {
            val executorServices = mock(ExecutorServices::class)
            val executor = mock(ScheduledExecutorService::class)
            val task = mock(PeriodicTask::class)
            val taskRunnableMock = mock(Runnable::class)
            val taskRunnable: () -> Unit = { taskRunnableMock.run() }
            val dispatcher = EventDispatcher.create(ServerListener::class.java)
            val future = mock(ScheduledFuture::class)

            val startupProcessor = TaskScheduler(executorServices, dispatcher, listOf(task))

            check {
                one(executorServices).normalExecutorService; will(returnValue(executor))

                one(task).task; will(returnValue(taskRunnable))
                one(task).initialDelay; will(returnValue(10L))
                one(task).delay; will(returnValue(11L))
                one(taskRunnableMock).run(); will(throwException(RuntimeException()))

                one(executor).scheduleAtFixedRate(with(any(Runnable::class.java)), with(10L), with(11L), with(TimeUnit.MILLISECONDS));
                will(object : CustomAction("") {
                    override fun invoke(p0: Invocation?): Any {
                        (p0!!.getParameter(0) as Runnable).run()
                        return future
                    }

                })
            }

            startupProcessor.init()
            startupProcessor.serverStartup()
        }
    }
}