package jetbrains.buildServer.sesPlugin.sqs

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.data.SESNotification
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

class SQSMessagesReaderImpl(private val sqsMessagesReceiver: SQSMessagesReceiver<SESNotification>,
                            messageHandlers: Collection<SQSMessageHandler>) : SQSMessagesReader {

    private val messageHandlers: Sequence<SQSMessageHandler> = messageHandlers.asSequence() + sequenceOf(UnknownMessageHandler())
    private val logger = Logger.getInstance(SQSMessagesReaderImpl::class.qualifiedName)

    private fun readQueue(sqsBean: SQSBean): Int {
        val receiveMessagesResult = sqsMessagesReceiver.receiveMessages(sqsBean)
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

                        messageHandlers.first {
                            it.accepts(eventType)
                        }.apply {
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

}