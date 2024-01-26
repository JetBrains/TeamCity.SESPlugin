

package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.data.SQSBeanValidatorResult

interface SQSBeanValidator {
    fun validate(bean: SQSBean): SQSBeanValidatorResult
}