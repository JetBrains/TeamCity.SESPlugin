package jetbrains.buildServer.sesPlugin.sqs

import com.google.gson.JsonObject
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SQSMessagesReaderImpl(private val sqsMessagesReceiverImpl: SQSMessagesReceiver,
                            messageHandlers: Collection<SQSMessageHandler>) : SQSMessagesReader {

    private val messageHandlers: Sequence<SQSMessageHandler> = messageHandlers.asSequence()

    private fun readQueue(sqsBean: SQSBean): Int {
        val messages = sqsMessagesReceiverImpl.receiveMessages(sqsBean).messages

        messages.forEach {
            if (it.result != null) {
                val mailMessage = it.result.Message
                val eventType = mailMessage["eventType"].asString

                messageHandlers.find(eventType) {
                    handle(mailMessage)
                }
            } else if (it.exception != null) {

            }
        }

        return messages.size
    }

    override fun readAllQueues(beans: Sequence<SQSBean>): Int {
        return beans.map { readQueue(it) }.sum()
    }


    private fun Sequence<SQSMessageHandler>.find(type: String, f: SQSMessageHandler.() -> Unit) {

        val find = this.find {
            it.accepts(type)
        } ?: UnknownMessageHandler()

        find.apply(f)
    }

    class UnknownMessageHandler : SQSMessageHandler {
        override fun accepts(type: String) = true

        override fun handle(data: JsonObject) {
            // todo log
        }
    }
}