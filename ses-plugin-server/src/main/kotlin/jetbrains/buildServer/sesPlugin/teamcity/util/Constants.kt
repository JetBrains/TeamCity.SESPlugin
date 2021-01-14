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

@Suppress("PropertyName")
class Constants {
    val ACCOUNT_ID_PARAM = static.ACCOUNT_ID_PARAM
    val ACCOUNT_ID_LABEL = static.ACCOUNT_ID_LABEL
    val QUEUE_NAME_PARAM = static.QUEUE_NAME_PARAM
    val QUEUE_NAME_LABEL = static.QUEUE_NAME_LABEL
    val ENABLED = static.ENABLED

    companion object static {
        val ACCOUNT_ID_PARAM = "aws.sesIntegration.accountId"
        val ACCOUNT_ID_LABEL = "Owner Account ID"
        val QUEUE_NAME_PARAM = "aws.sesIntegration.queueName"
        val QUEUE_NAME_LABEL = "SQS Queue Name"
        val ENABLED = "aws.sesIntegration.enabled"
    }
}