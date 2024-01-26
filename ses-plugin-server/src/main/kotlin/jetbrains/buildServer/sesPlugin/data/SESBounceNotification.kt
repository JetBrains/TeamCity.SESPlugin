

package jetbrains.buildServer.sesPlugin.data

interface SESBounceNotification : SESNotification {
    var bounce: BounceData
    fun getBounceSubType(): String
    fun getRecipients(): List<Recipient>
    fun getBounceType(): String
}