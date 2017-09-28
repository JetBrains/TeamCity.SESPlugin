package jetbrains.buildServer.sesPlugin.dataHolders

import com.google.gson.annotations.SerializedName

class NotificationRequestData(@SerializedName("Type") override val type: String,
                              @SerializedName("MessageId") override val messageId: String,
                              @SerializedName("TopicArn") override val topicArn: String,
                              @SerializedName("Message") override val message: String,
                              @SerializedName("Timestamp") override val timestamp: String,
                              @SerializedName("SignatureVersion") override val signatureVersion: String,
                              @SerializedName("Signature") override val signature: String,
                              @SerializedName("SigningCertURL") override val signingCertURL: String,
                              @SerializedName("Subject") val subject: String,
                              @SerializedName("UnsubscribeURL") val unsubscribeURL: String) : RequestData {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationRequestData

        if (type != other.type) return false
        if (messageId != other.messageId) return false
        if (topicArn != other.topicArn) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false
        if (signatureVersion != other.signatureVersion) return false
        if (signature != other.signature) return false
        if (signingCertURL != other.signingCertURL) return false
        if (subject != other.subject) return false
        if (unsubscribeURL != other.unsubscribeURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + messageId.hashCode()
        result = 31 * result + topicArn.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + signatureVersion.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + signingCertURL.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + unsubscribeURL.hashCode()
        return result
    }
}