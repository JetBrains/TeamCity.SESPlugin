/*
 * Copyright 2000-2021 JetBrains s.r.o.
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