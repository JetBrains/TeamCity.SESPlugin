package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.sqs.data.BounceData
import jetbrains.buildServer.sesPlugin.sqs.data.SESNotificationData

class SQSBounceMessageHandler(private val bounceHandler: BounceHandler) : SQSMessageHandler {
    override fun accepts(type: String) = type == "Bounce"

    override fun handle(data: SESNotificationData) {
        if (isCriticalBounce(data.bounce)) {
            if (data.bounce.bounceSubType == "Suppressed") {
                // todo inform admin
            }

            for (recipient in data.bounce.bouncedRecipients) {
                val email = recipient.emailAddress
                val action = recipient.action
                val failed = (action == "failed")
                val diagnostics = recipient.diagnosticCode

                if (failed) {
                    bounceHandler.handleBounce(email)
                }
            }
        } else if (isHandableBounce(data)) {
            when (data.bounce.bounceSubType) {
                "MessageTooLarge" -> {
                }
                "ContentRejected" -> {
                }
                "AttachmentRejected" -> {
                }
                "General" -> {
                }
                "MailboxFull" -> {
                }
            }
        }
    }

    private fun isHandableBounce(data: SESNotificationData) = data.bounce.bounceType == "Transient"

    private fun isPermanent(data: BounceData) = data.bounceType == "Permanent"

    private fun isUndetermined(data: BounceData) = data.bounceType == "Undetermined"

    private fun isCriticalBounce(data: BounceData) = isPermanent(data) || isUndetermined(data)
}