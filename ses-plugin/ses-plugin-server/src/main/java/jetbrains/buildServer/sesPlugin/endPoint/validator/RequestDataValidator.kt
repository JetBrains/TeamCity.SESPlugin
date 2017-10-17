package jetbrains.buildServer.sesPlugin.endPoint.validator

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData
import java.util.concurrent.CompletableFuture

interface RequestDataValidator {

    fun validate(data: RequestData): CompletableFuture<ValidationResult>

    data class ValidationResult(val isCorrect: Boolean, val exception: Exception? = null)
}