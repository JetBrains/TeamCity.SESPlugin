package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.util.UserSetProvider
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class BounceHandlerImplTest {

    @Test
    fun testBulkHandle() {
        mocking {
            val userBounceHandler = mock(UserBounceHandler::class)
            val userSetProvider = mock(UserSetProvider::class)
            val user1 = mock(SUser::class, "user1")
            val user2 = mock(SUser::class, "user2")

            val handler = BounceHandlerImpl(userSetProvider, listOf(userBounceHandler))

            val mail1 = "mail1"
            val mail2 = "mail2"

            check {
                one(userSetProvider).users; will(Expectations.returnValue(setOf(user1, user2)))
                one(user2).email; will(Expectations.returnValue("mail1"))
                one(user1).email; will(Expectations.returnValue("mail2"))
                one(userBounceHandler).handleBounce(user1)
                one(userBounceHandler).handleBounce(user2)
            }

            handler.handleBounces(sequenceOf(mail1, mail2))
        }
    }
}