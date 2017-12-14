package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.teamcity.SESIntegrationManager
import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.sesPlugin.teamcity.util.ConfigurableLong
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.jmock.Expectations
import org.testng.annotations.Test

class RescanSQSTaskImplTest {
    @Test
    fun test() {
        mocking {
            val reader = mock(SQSMessagesReader::class)
            val manager = mock(SESIntegrationManager::class)
            val list = listOf(mock(SQSBean::class)).asSequence()
            val delay = mock(ConfigurableLong::class, "delay")
            val initDelay = mock(ConfigurableLong::class, "initDelay")

            check {
                one(reader).readAllQueues(list)
                one(manager).getBeans(""); will(Expectations.returnValue(list))
            }

            RescanSQSTaskImpl(reader, manager, delay, initDelay).task.invoke()
        }
    }
}