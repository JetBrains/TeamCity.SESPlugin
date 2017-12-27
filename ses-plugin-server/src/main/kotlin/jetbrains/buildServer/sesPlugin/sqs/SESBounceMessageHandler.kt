package jetbrains.buildServer.sesPlugin.sqs

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.SESBounceNotification
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService
import jetbrains.buildServer.sesPlugin.teamcity.util.NoOpLogService

class SESBounceMessageHandler(private val bounceHandler: BounceHandler,
                              private val logService: LogService = NoOpLogService()) : SQSMessageHandler {
    private val logger = Logger.getInstance(SESBounceMessageHandler::class.qualifiedName)

    override fun accepts(type: String) = type == "Bounce"

    override fun handle(data: SESNotification) {
        if (data !is SESBounceNotification) throw IllegalArgumentException()
        val bounceType = data.getBounceType()

        when {
            isCriticalBounce(bounceType) -> {
                for (recipient in data.getRecipients()) {
                    val email = recipient.emailAddress

                    logService.log {
                        if (data.getBounceSubType() == "Suppressed") {
                            logger.warn("Got suppressed bounce for email '$email': $data")
                        } else {
                            logger.info("Got hard bounce for email '$email': $data")
                        }
                    }

                    try {
                        bounceHandler.handleBounce(email)
                    } catch (e: Exception) {
                        logService.log {
                            logger.warnAndDebugDetails("Exception occurred while handling bounce '$data' with '$bounceHandler'", e)
                        }
                    }
                }
            }

            isHandableBounce(bounceType) -> {
                logger.info("Got soft bounce (${data.getBounceSubType()})")
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