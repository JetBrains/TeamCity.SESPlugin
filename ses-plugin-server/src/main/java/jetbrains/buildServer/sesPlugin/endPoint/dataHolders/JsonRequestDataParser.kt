package jetbrains.buildServer.sesPlugin.endPoint.dataHolders

import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader

class JsonRequestDataParser {

    companion object {
        const val SUBSCRIBE_TYPE = "SubscriptionConfirmation"
        const val UNSUBSCRIBE_TYPE = "UnsubscribeConfirmation"
        const val NOTIFICATION_TYPE = "Notification"
    }

    fun parse(type: String?, inputStream: InputStream): RequestData {

        val gson = Gson()
        val holder = when (type) {
            SUBSCRIBE_TYPE -> {
                gson.fromJson<SubscribeRequestData>(InputStreamReader(inputStream, "UTF-8"), SubscribeRequestData::class.java)
            }
            UNSUBSCRIBE_TYPE -> {
                gson.fromJson<UnsubscribeRequestData>(InputStreamReader(inputStream, "UTF-8"), UnsubscribeRequestData::class.java)
            }
            NOTIFICATION_TYPE -> {
                gson.fromJson<NotificationRequestData>(InputStreamReader(inputStream, "UTF-8"), NotificationRequestData::class.java)
            }
            null -> throw IllegalArgumentException("No type provided")
            else -> throw IllegalArgumentException("Unknown type $type")
        }

        if (holder.type != type) throw TypeMismatchException("Type mismatch. Expected $type but got ${holder.type}")

        return holder
    }

    class TypeMismatchException(message: String?) : Exception(message)
}