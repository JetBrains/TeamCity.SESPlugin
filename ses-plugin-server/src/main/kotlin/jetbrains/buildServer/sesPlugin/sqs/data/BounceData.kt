package jetbrains.buildServer.sesPlugin.sqs.data

data class BounceData(
        var bounceType: String,
        var bounceSubType: String,
        var bouncedRecipients: List<Recipient>,
        var timestamp: String,
        var feedbackId: String,
        var reportingMTA: String
)