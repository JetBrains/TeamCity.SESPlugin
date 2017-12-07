package jetbrains.buildServer.sesPlugin.endPoint.validator

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData

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