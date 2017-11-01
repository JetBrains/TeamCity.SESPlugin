package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.JsonObject

interface SQSMessageHandler {
    fun accepts(type: String): Boolean
    fun handle(data: JsonObject)
}