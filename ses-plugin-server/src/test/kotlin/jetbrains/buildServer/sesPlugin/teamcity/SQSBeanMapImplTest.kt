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

package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.sesPlugin.util.then
import org.assertj.core.api.BDDAssertions.then
import org.testng.annotations.Test

class SQSBeanMapImplTest {
    @Test
    fun testIsDisabled() {
        then(SQSBeanMapImpl(mapOf()).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "false")).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "asdfas")).isDisabled()).isTrue()
        then(SQSBeanMapImpl(mapOf(Constants.ENABLED to "true")).isDisabled()).isFalse()
    }

    @Test
    fun testAccountId() {
        then(SQSBeanMapImpl(mapOf(Constants.ACCOUNT_ID_PARAM to "account")).accountId).isEqualTo("account")
        then(SQSBeanMapImpl(mapOf(Constants.ACCOUNT_ID_PARAM to "account1")).accountId).isEqualTo("account1")
        then({ SQSBeanMapImpl(mapOf()).accountId }).isNotNull().isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun testQueueName() {
        then(SQSBeanMapImpl(mapOf(Constants.QUEUE_NAME_PARAM to "queue")).queueName).isEqualTo("queue")
        then(SQSBeanMapImpl(mapOf(Constants.QUEUE_NAME_PARAM to "queue1")).queueName).isEqualTo("queue1")
        then({ SQSBeanMapImpl(mapOf()).queueName }).isNotNull().isInstanceOf(IllegalStateException::class.java)
    }
}