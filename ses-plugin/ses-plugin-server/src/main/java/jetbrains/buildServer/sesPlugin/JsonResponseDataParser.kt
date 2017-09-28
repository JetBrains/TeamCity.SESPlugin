package jetbrains.buildServer.sesPlugin

import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader

class JsonResponseDataParser {

    companion object {
        const val SUBSCRIBE_TYPE = "SubscriptionConfirmation"
        const val UNSUBSCRIBE_TYPE = "UnsubscribeConfirmation"
        const val NOTIFICATION_TYPE = "Notification"
    }

    fun parse(type: String?, inputStream: InputStream): ResponseDataHolder {

        val gson = Gson()
        val holder = when (type) {
            SUBSCRIBE_TYPE -> {
                gson.fromJson<SubscribeResponseDataHolder>(InputStreamReader(inputStream, "UTF-8"), SubscribeResponseDataHolder::class.java)
            }
            UNSUBSCRIBE_TYPE -> {
                gson.fromJson<UnsubscribeResponseDataHolder>(InputStreamReader(inputStream, "UTF-8"), UnsubscribeResponseDataHolder::class.java)
            }
            NOTIFICATION_TYPE -> {
                gson.fromJson<NotificationResponseDataHolder>(InputStreamReader(inputStream, "UTF-8"), NotificationResponseDataHolder::class.java)
            }
            null -> throw IllegalArgumentException("No type provided")
            else -> throw IllegalArgumentException("Unknown type $type")
        }

        if (holder.type != type) throw TypeMismatchException("Type mismatch. Expected $type but got ${holder.type}")

        return holder
    }

    class TypeMismatchException(message: String?) : Exception(message)
}