package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SESBean

interface SQSBounceMessagesReader {
    fun readAllQueues(beans: Sequence<SESBean>): Int
}