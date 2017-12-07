package jetbrains.buildServer.sesPlugin.endPoint.validator

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData

interface RequestDataValidatorFactory {
    fun getValidator(data: RequestData): RequestDataValidator
}