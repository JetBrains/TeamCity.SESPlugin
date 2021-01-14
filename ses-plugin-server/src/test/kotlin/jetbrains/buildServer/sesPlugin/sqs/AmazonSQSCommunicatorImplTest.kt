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

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.*
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.jmock.api.Invocation
import org.jmock.lib.action.ActionSequence
import org.jmock.lib.action.CustomAction
import org.testng.annotations.Test

@Suppress("UNCHECKED_CAST")
class AmazonSQSCommunicatorImplTest {
    @Test
    fun testPerformTask() {
        mocking {
            val bean = mock(SQSBean::class)
            val task = mock(AmazonSQSCommunicatorTask::class) as AmazonSQSCommunicatorTask<Any>

            val provider: AWSClientsProvider = mock(AWSClientsProvider::class)
            val factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class)
            val queueUrl: QueueUrlProvider = mock(QueueUrlProvider::class)

            val clients = mock(SQSAWSClients::class)
            val amazonSQS = mock(AutoCloseableAmazonSQS::class)

            val result = "The result"

            check {
                one(factory).createAmazonSQSClient(clients); will(returnValue(amazonSQS))
                one(queueUrl).getQueueUrl(amazonSQS, bean); will(returnValue("someQueue"))

                one(task).perform(amazonSQS, "someQueue"); will(returnValue(result))
                one(amazonSQS).close()
            }
            invocation {
                func("withClient")
                on(provider)
                will(
                        ActionSequence(
                                object : CustomAction("call the callback") {
                                    override fun invoke(p0: Invocation?): Any {
                                        if (p0 != null) {
                                            return (p0.getParameter(1) as Function1<SQSAWSClients, String>).invoke(clients)
                                        } else {
                                            throw RuntimeException("")
                                        }
                                    }
                                }, returnValue(clients)
                        )

                )
                count(1)
            }

            BDDAssertions.then(receiver(provider, factory, queueUrl).performTask(bean, task)).isSameAs(result)
        }
    }

    private fun Mockery.receiver(provider: AWSClientsProvider = mock(AWSClientsProvider::class),
                                 factory: AmazonSQSClientFactory = mock(AmazonSQSClientFactory::class),
                                 queueUrl: QueueUrlProvider = mock(QueueUrlProvider::class)): AmazonSQSCommunicatorImpl = AmazonSQSCommunicatorImpl(provider, factory, queueUrl)
}