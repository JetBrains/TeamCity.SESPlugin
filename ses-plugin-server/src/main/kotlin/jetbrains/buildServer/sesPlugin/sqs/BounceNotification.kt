package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.Recipient

interface BounceNotification {
    fun getBounceSubType(): String
    fun getRecipients(): List<Recipient>
    fun getBounceType(): String

}