

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