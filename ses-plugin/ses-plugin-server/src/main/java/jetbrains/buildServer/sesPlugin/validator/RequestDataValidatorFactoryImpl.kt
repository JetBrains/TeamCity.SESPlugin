package jetbrains.buildServer.sesPlugin.validator

import jetbrains.buildServer.sesPlugin.dataHolders.RequestData

class RequestDataValidatorFactoryImpl : RequestDataValidatorFactory {
    override fun getValidator(data: RequestData): RequestDataValidator {
        when (data.signatureVersion) {
            "1" -> {
                return RequestDataValidatorV1Impl()
            }
            else -> {
                throw SecurityException("Unknown signature version: ${data.signatureVersion}")
            }
        }
    }
}