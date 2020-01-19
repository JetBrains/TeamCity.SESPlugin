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

import jetbrains.buildServer.sesPlugin.data.AmazonSQSCommunicationResult
import jetbrains.buildServer.sesPlugin.data.AmazonSQSNotification
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.AmazonSQSCommunicator
import jetbrains.buildServer.sesPlugin.sqs.awsCommunication.ReceiveMessagesTask
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class MessagesReceiverFacade(private val amazonSQSCommunicator: AmazonSQSCommunicator,
                             private val receiveMessagesTask: ReceiveMessagesTask) : SQSMessagesReceiver<AmazonSQSNotification> {
    override fun receiveMessages(bean: SQSBean): AmazonSQSCommunicationResult<AmazonSQSNotification> {
        return try {
            amazonSQSCommunicator.performTask(bean, receiveMessagesTask)
        } catch (ex: Exception) {
            AmazonSQSCommunicationResult(emptyList(), ex, ex.message ?: ex.javaClass.name)
        }
    }
}