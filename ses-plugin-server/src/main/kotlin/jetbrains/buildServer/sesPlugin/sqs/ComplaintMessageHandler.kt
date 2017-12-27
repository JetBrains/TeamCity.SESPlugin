package jetbrains.buildServer.sesPlugin.sqs

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.SESComplaintNotification
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.util.LogService

class ComplaintMessageHandler(private val bounceHandler: BounceHandler,
                              private val logService: LogService) : SQSMessageHandler {

    private val logger: Logger = Logger.getInstance(ComplaintMessageHandler::class.qualifiedName)

    override fun handle(data: SESNotification) {
        if (data !is SESComplaintNotification) throw IllegalArgumentException()

        for (complainedRecipient in data.getComplainedRecipients()) {
            try {
                bounceHandler.handleBounce(complainedRecipient.emailAddress)
            } catch (e: Exception) {
                logService.log {
                    logger.warnAndDebugDetails("Exception occurred while handling bounce '$data' with '$bounceHandler'", e)
                }
            }
            complainedRecipient.emailAddress
        }

    }

    override fun accepts(type: String) = type == "Compliant"
}