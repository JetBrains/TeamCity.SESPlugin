package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager

class RescanSQSTaskImpl(private val queueReader: SQSMessagesReader,
                        private val sesIntegrationManager: SESIntegrationManager) : Runnable {
    override fun run() {
        queueReader.readAllQueues(sesIntegrationManager.getBeans("").asSequence())
    }
}