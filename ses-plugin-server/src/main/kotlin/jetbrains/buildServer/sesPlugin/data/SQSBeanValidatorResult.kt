package jetbrains.buildServer.sesPlugin.data

data class SQSBeanValidatorResult(
        val status: Boolean = true,
        val description: String = "correct",
        val errorFields: Collection<String> = emptyList()
)