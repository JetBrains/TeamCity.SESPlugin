

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.data.CheckConnectionResult
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean

interface SQSConnectionChecker {
    fun checkConnection(bean: SQSBean): CheckConnectionResult
}