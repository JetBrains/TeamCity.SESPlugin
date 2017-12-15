package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.*
import java.lang.reflect.Type

class SQSNotificationParserImpl : SQSNotificationParser {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(AmazonSQSNotification::class.java, AmazonSQSNotificationDeserializer())
        gson = gsonBuilder.create()
    }

    override fun parse(data: String): AmazonSQSNotificationParseResult<AmazonSQSNotification> {
        var res: AmazonSQSNotification?
        var ex: SQSNotificationParseException?
        try {
            res = gson.fromJson<AmazonSQSNotification>(data, AmazonSQSNotification::class.java)
            ex = validate(res, data)
        } catch (cause: Exception) {
            res = null
            ex = SQSNotificationParseException("Cannot parse data ($data)", cause)
        }

        return AmazonSQSNotificationParseResult(res, ex)
    }

    @Suppress("SENSELESS_COMPARISON") // GSON
    private fun validate(res: AmazonSQSNotification, data: String): SQSNotificationParseException? {
        if (res.Message == null) {
            return SQSNotificationParseException("Cannot parse data: message should not be empty ($data)")
        }
        if (res.Type == null) {
            return SQSNotificationParseException("Cannot parse data: type should not be empty ($data)")
        }
        return null
    }

    inner class AmazonSQSNotificationDeserializer : JsonDeserializer<AmazonSQSNotification> {
        override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): AmazonSQSNotification {
            val obj = element.asJsonObject

            val Type = obj["Type"]?.asString ?: throw JsonParseException("Type not found")
            val MessageId = obj["MessageId"]?.asString ?: throw JsonParseException("MessageId not found")
            val TopicArn = obj["TopicArn"]?.asString ?: throw JsonParseException("TopicArn not found")
            val Subject = obj["Subject"]?.asString ?: throw JsonParseException("Subject not found")
            val Message = obj["Message"]?.asString ?: throw JsonParseException("Message not found")
            val Timestamp = obj["Timestamp"].asString ?: throw JsonParseException("Timestamp not found")
            val SignatureVersion = obj["SignatureVersion"].asString ?: throw JsonParseException("SignatureVersion not found")
            val Signature = obj["Signature"].asString ?: throw JsonParseException("Signature not found")
            val SigningCertURL = obj["SigningCertURL"].asString ?: throw JsonParseException("SigningCertURL not found")
            val UnsubscribeURL = obj["UnsubscribeURL"]?.asString

            return AmazonSQSNotification(Type, MessageId, TopicArn, Subject, Message, Timestamp, SignatureVersion, Signature, SigningCertURL, UnsubscribeURL)
        }

    }
}