

package jetbrains.buildServer.sesPlugin.data

data class UnknownSESNotification(override var eventType: String, override var mail: MailData) : SESNotification