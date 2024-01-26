

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.ReadQueueResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface SQSMessagesReader {
    fun readAllQueues(beans: Sequence<SQSBean>): ReadQueueResult
}