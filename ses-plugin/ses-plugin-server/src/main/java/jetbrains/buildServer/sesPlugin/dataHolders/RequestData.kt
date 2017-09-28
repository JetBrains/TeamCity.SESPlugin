package jetbrains.buildServer.sesPlugin.dataHolders

interface RequestData {
    val type: String
    val messageId: String
    val topicArn: String
    val message: String
    val timestamp: String
    val signatureVersion: String
    val signature: String
    val signingCertURL: String
}