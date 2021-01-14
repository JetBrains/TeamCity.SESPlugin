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

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.SQSNotificationParser
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService
import jetbrains.buildServer.sesPlugin.teamcity.util.TeamCityProperties

class ReceiveMessagesTask(private val properties: TeamCityProperties,
                          private val sqsNotificationParser: SQSNotificationParser,
                          private val loggerService: LogService = NoOpLogService()) : AmazonSQSCommunicatorTask<AmazonSQSCommunicationResult<AmazonSQSNotification>> {
    private val logger = Logger.getInstance(this::class.qualifiedName)

    private fun prepareRequest() =
            ReceiveMessageRequest().withMaxNumberOfMessages(properties.getInt("teamcity.sesIntegration.maxNumberOfMessages", 10))

    override fun perform(sqs: AmazonSQS, queueUrl: String): AmazonSQSCommunicationResult<AmazonSQSNotification> {
        val messagesResult = try {
            sqs.receiveMessage(prepareRequest().withQueueUrl(queueUrl))
        } catch (ex: Exception) {
            return AmazonSQSCommunicationResult(emptyList(), ex, "Cannot communicate with Amazon SQS")
        }

        if (messagesResult.messages.isEmpty()) {
            return AmazonSQSCommunicationResult(emptyList(), null, "No new messages")
        }

        if (properties.getBoolean("teamcity.sesIntegration.markMessagesAsUnread", false)) {
            for (i in messagesResult.messages) {
                if (Thread.currentThread().isInterrupted) return AmazonSQSCommunicationResult(emptyList(), null, "Execution is interrupted")

                sqs.changeMessageVisibility(ChangeMessageVisibilityRequest().withQueueUrl(queueUrl).withReceiptHandle(i.receiptHandle).withVisibilityTimeout(0))
            }
        } else {
            if (properties.getBoolean("teamcity.sesIntegration.deleteReadMessages", true)) {
                val r = DeleteMessageBatchRequest().withQueueUrl(queueUrl)
                messagesResult.messages.forEach {
                    r.entries.add(DeleteMessageBatchRequestEntry(it.messageId, it.receiptHandle))
                }
                try {
                    sqs.deleteMessageBatch(r)
                } catch (e: Exception) {
                    loggerService.log {
                        logger.warnAndDebugDetails("Cannot delete processed messages from '$queueUrl'", e)
                    }
                }
            }
        }

        if (Thread.currentThread().isInterrupted) return AmazonSQSCommunicationResult(emptyList(), null, "Execution is interrupted")

        return messagesResult.messages.map {
            sqsNotificationParser.parse(it.body)
        }.let {
            AmazonSQSCommunicationResult(it)
        }
    }
}