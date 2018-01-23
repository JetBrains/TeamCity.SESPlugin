package jetbrains.buildServer.sesPlugin.data

import java.util.*

data class ReadQueueResult(val successful: Boolean,
                           val description: String,
                           val countProcessed: Int = 0,
                           val countFailed: Int = 0,
                           val exception: Optional<Exception> = Optional.empty()) {
    operator fun plus(other: ReadQueueResult): ReadQueueResult {
        return ReadQueueResult(successful && other.successful, description, countProcessed + other.countProcessed, countFailed + other.countFailed, exception.flatMap { other.exception })
    }
}