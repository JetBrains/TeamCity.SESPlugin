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

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.SESBounceNotification
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService

class BounceMessageHandler(private val bounceHandler: BounceHandler,
                           private val logService: LogService = NoOpLogService()) : SQSMessageHandler {
    private val logger = Logger.getInstance(BounceMessageHandler::class.qualifiedName)

    override fun accepts(type: String) = type == "Bounce"

    override fun handle(data: SESNotification) {
        if (data !is SESBounceNotification) throw IllegalArgumentException()
        val bounceType = data.getBounceType()

        when {
            isCriticalBounce(bounceType) -> {
                val emails = data.getRecipients().asSequence().map {
                    val email = it.emailAddress

                    logService.log {
                        if (data.getBounceSubType() == "Suppressed") {
                            if (logger.isDebugEnabled) {
                                logger.info("Got suppressed bounce for email '$email'")
                            } else {
                                logger.debug("Got suppressed bounce for email '$email': $data")
                            }
                        } else {
                            if (logger.isDebugEnabled) {
                                logger.debug("Got hard bounce for email '$email': $data")
                            } else {
                                logger.info("Got hard bounce for email '$email'")
                            }
                        }
                    }

                    email
                }

                try {
                    bounceHandler.handleBounces(emails)
                } catch (e: Exception) {
                    logService.log {
                        logger.warnAndDebugDetails("Exception occurred while handling bounces", e)
                    }
                }
            }

            isHandableBounce(bounceType) -> {
                logger.debug("Got soft bounce (${data.getBounceSubType()})")
//                when (data.getBounceSubType()) {
//                    "MessageTooLarge" -> {
//                    }
//                    "ContentRejected" -> {
//                    }
//                    "AttachmentRejected" -> {
//                    }
//                    "General" -> {
//                    }
//                    "MailboxFull" -> {
//                    }
//                    else -> {
//                    }
//                }
            }

            else -> logService.log {
                logger.warn("Got unknown bounce type $data")
            }
        }
    }

    private fun isHandableBounce(bounceType: String) = bounceType == "Transient"

    private fun isCriticalBounce(bounceType: String) = bounceType == "Permanent" || bounceType == "Undetermined"
}