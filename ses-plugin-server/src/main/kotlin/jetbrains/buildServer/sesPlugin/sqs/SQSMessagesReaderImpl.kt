package jetbrains.buildServer.sesPlugin.sqs

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.sqs.data.SESNotificationData
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SQSMessagesReaderImpl(private val sqsMessagesReceiverImpl: SQSMessagesReceiver<SESNotificationData>,
                            messageHandlers: Collection<SQSMessageHandler>) : SQSMessagesReader {

    private val messageHandlers: Sequence<SQSMessageHandler> = messageHandlers.asSequence()
    private val logger = Logger.getInstance(SQSMessagesReaderImpl::class.qualifiedName)

    private fun readQueue(sqsBean: SQSBean): Int {
        val receiveMessagesResult = sqsMessagesReceiverImpl.receiveMessages(sqsBean)
        if (receiveMessagesResult.exception != null) {
            logger.warnAndDebugDetails("Cannot read Amazon SQS queue: ${receiveMessagesResult.description}", receiveMessagesResult.exception)

            return 0
        } else {
            val messages = receiveMessagesResult.messages

            var processed = 0

            messages.forEach {
                if (it.result != null) {
                    try {
                        val message = it.result

                        val eventType = message.eventType

                        messageHandlers.find(eventType) {
                            handle(message)
                            processed++
                        }
                    } catch (e: Exception) {
                        logger.warnAndDebugDetails("Error processing message", e)
                    }
                } else if (it.exception != null) {
                    logger.debug("Error parsing message", receiveMessagesResult.exception)
                }
            }

            return processed
        }
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

        override fun handle(data: SESNotificationData) {
            // todo log
        }
    }
}