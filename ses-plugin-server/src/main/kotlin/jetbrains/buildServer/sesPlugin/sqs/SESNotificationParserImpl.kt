package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.Gson
import jetbrains.buildServer.sesPlugin.data.SESNotificationData

class SESNotificationParserImpl : SESNotificationParser {
    private val gson: Gson = Gson()

    override fun parse(data: String): SESNotificationData {
        return gson.fromJson(data, SESNotificationData::class.java)
    }
}