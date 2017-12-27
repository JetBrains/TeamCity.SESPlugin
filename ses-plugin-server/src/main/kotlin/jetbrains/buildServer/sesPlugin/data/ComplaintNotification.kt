package jetbrains.buildServer.sesPlugin.data

data class ComplaintNotification(
        override var eventType: String,
        override var mail: MailData,
        override var complaint: ComplaintData) : SESComplaintNotification {
    override fun getComplainedRecipients(): Sequence<Recipient> = complaint.complainedRecipients.asSequence()
}