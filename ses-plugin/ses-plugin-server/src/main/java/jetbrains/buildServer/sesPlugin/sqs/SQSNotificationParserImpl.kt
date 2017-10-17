package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.Gson

class SQSNotificationParserImpl : SQSNotificationParser {
    private val gson = Gson()

    override fun parse(data: String): AmazonSQSNotificationParseResult {
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
        if (res.message == null) {
            return SQSNotificationParseException("Cannot parse data: message should not be empty ($data)")
        }
        if (res.type == null) {
            return SQSNotificationParseException("Cannot parse data: type should not be empty ($data)")
        }
        return null
    }
}