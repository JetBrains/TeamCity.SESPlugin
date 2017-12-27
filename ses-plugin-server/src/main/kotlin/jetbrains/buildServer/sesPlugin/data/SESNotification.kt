package jetbrains.buildServer.sesPlugin.data

interface SESNotification {
    var eventType: String
    var mail: MailData
}