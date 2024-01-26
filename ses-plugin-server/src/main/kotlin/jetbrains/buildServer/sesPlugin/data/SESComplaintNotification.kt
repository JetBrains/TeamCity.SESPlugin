

package jetbrains.buildServer.sesPlugin.data

interface SESComplaintNotification : SESNotification {
    var complaint: ComplaintData
    fun getComplainedRecipients(): Sequence<Recipient>
}