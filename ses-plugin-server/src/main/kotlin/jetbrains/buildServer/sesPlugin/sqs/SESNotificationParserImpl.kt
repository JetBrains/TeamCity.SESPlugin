/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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