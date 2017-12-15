package jetbrains.buildServer.sesPlugin.sqs.data

data class Recipient(
        var emailAddress: String,
        var action: String,
        var status: String,
        var diagnosticCode: String
)