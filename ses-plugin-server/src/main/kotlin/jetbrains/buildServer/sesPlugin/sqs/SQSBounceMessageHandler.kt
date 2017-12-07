package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.JsonObject
import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler

class SQSBounceMessageHandler(private val bounceHandler: BounceHandler) : SQSMessageHandler {
    override fun accepts(type: String) = type == "Bounce"

    override fun handle(data: JsonObject) {
        val data2 = data["bounce"].asJsonObject
        if (isCriticalBounce(data2)) {
            if (subtype(data2) == "Suppressed") {
                // todo inform admin
            }

            for (recipient in bounces(data2)) {
                val email = bounceMail(recipient)
                val action = bounceAction(recipient)
                val failed = (action == "failed")
                val diagnostics = diagnostics(recipient)

                if (failed) {
                    bounceHandler.handleBounce(email)
                }
            }
        } else if (isHandableBounce(data2)) {
            when (subtype(data2)) {
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

    private fun subtype(data: JsonObject) = data["bounceSubType"].asString

    private fun isHandableBounce(data: JsonObject) = data["bounceType"].asString == "Transient"

    private fun diagnostics(recipient: JsonObject) = recipient["diagnosticCode"]

    private fun bounceAction(recipient: JsonObject) = recipient["action"].asString

    private fun bounceMail(recipient: JsonObject) = recipient["emailAddress"].asString

    private fun bounces(data: JsonObject): List<JsonObject> {
        return if (data["bouncedRecipients"] == null || !data["bouncedRecipients"].isJsonArray) emptyList() else data["bouncedRecipients"].asJsonArray.map { it.asJsonObject }
    }

    private fun isPermanent(data: JsonObject) = data["bounceType"].asString == "Permanent"

    private fun isUndetermined(data: JsonObject) = data["bounceType"].asString == "Undetermined"

    private fun isCriticalBounce(data: JsonObject) = isPermanent(data) || isUndetermined(data)
}