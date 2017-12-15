package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.Gson

class SESNotificationParserImpl : SESNotificationParser {
    private val gson: Gson = Gson()

    override fun parse(data: String): SESNotificationData {
        return gson.fromJson(data, SESNotificationData::class.java)
    }
}