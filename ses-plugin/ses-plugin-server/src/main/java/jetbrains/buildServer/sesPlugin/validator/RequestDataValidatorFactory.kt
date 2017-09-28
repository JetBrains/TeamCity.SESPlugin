package jetbrains.buildServer.sesPlugin.validator

import jetbrains.buildServer.sesPlugin.dataHolders.RequestData

interface RequestDataValidatorFactory {
    fun getValidator(data: RequestData): RequestDataValidator
}