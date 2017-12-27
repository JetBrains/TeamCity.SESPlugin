package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.*
import jetbrains.buildServer.sesPlugin.data.BounceNotification
import jetbrains.buildServer.sesPlugin.data.ComplaintNotification
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.data.UnknownSESNotification
import java.lang.reflect.Type

class SESNotificationParserImpl : SESNotificationParser {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(SESNotification::class.java, SESNotificationDeserializer())
        gson = gsonBuilder.create()
    }

    override fun parse(data: String): SESNotification {
        return gson.fromJson(data, SESNotification::class.java)
    }
}

class SESNotificationDeserializer : JsonDeserializer<SESNotification> {
    override fun deserialize(el: JsonElement, type: Type, context: JsonDeserializationContext): SESNotification {
        if (el is JsonObject) {
            val eventType = el["eventType"]?.asString ?: throw JsonSyntaxException("eventType is not defined in $el")

            val realType = when (eventType) {
                "Bounce" -> BounceNotification::class.java
                "Complaint" -> ComplaintNotification::class.java
                else -> UnknownSESNotification::class.java
            }

            return context.deserialize<SESNotification>(el, realType)
        }

        throw JsonSyntaxException("Expected an json object but got $el")
    }

}