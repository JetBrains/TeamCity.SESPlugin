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

import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotificationParseResult
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SESMessagesReceiver(private val sqsMessagesReceiver: SQSMessagesReceiver<AmazonSQSNotification>,
                          private val sesNotificationParser: SESNotificationParser) : SQSMessagesReceiver<SESNotification> {
    override fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<SESNotification> {
        val received = sqsMessagesReceiver.receiveMessages(bean)
        if (received.exception != null) return AmazonSQSCommunicationResult(emptyList(), received.exception, received.description)

        val res = ArrayList<AmazonSQSNotificationParseResult<SESNotification>>()
        received.messages.forEach {
            if (it.result != null) {
                try {
                    res.add(AmazonSQSNotificationParseResult(sesNotificationParser.parse(it.result.Message)))
                } catch (e: Exception) {
                    res.add(AmazonSQSNotificationParseResult(exception = SQSNotificationParseException(cause = e)))
                }
            } else {
                res.add(AmazonSQSNotificationParseResult(exception = it.exception))
            }
        }
        return AmazonSQSCommunicationResult(res)
    }

}