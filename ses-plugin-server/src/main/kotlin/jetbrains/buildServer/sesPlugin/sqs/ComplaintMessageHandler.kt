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

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.SESComplaintNotification
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService

class ComplaintMessageHandler(private val bounceHandler: BounceHandler,
                              private val logService: LogService = NoOpLogService()) : SQSMessageHandler {

    private val logger: Logger = Logger.getInstance(ComplaintMessageHandler::class.qualifiedName)

    override fun handle(data: SESNotification) {
        if (data !is SESComplaintNotification) throw IllegalArgumentException()

        val mails = data.getComplainedRecipients().asSequence().map {
            logService.log {
                if (logger.isDebugEnabled) {
                    logger.debug("Got complaint for email '${it.emailAddress}': $data")
                } else {
                    logger.info("Got complaint for email '${it.emailAddress}'")
                }
            }

            it.emailAddress
        }

        try {
            bounceHandler.handleBounces(mails)
        } catch (e: Exception) {
            logService.log {
                logger.warnAndDebugDetails("Exception occurred while handling bounce '$data' with '$bounceHandler'", e)
            }
        }

    }

    override fun accepts(type: String) = type == "Complaint"
}