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

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueUrlResult
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.QueueUrlProviderImpl
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.jmock.Expectations.returnValue
import org.jmock.Expectations.throwException
import org.testng.annotations.Test

class QueueUrlProviderImplTest {
    @Test
    fun testGet() {
        mocking {
            val sqs = mock(AmazonSQS::class)
            val bean = mock(SQSBean::class)

            check {
                one(bean).queueName; will(returnValue("aaa"))
                one(bean).accountId; will(returnValue("bbb"))
            }
            invocation {
                on(sqs)
                func("getQueueUrl")
                will(returnValue(GetQueueUrlResult().withQueueUrl("some")))
            }

            then(QueueUrlProviderImpl().getQueueUrl(sqs, bean)).isEqualTo("some")
        }
    }

    @Test
    fun testError() {
        mocking {
            val sqs = mock(AmazonSQS::class)
            val bean = mock(SQSBean::class)

            val initialException = IllegalStateException()

            check {
                one(bean).queueName; will(returnValue("aaa"))
                one(bean).accountId; will(throwException(initialException))
            }

            jetbrains.buildServer.sesPlugin.util.then { (QueueUrlProviderImpl().getQueueUrl(sqs, bean)) }.isSameAs(initialException)
        }
    }
}