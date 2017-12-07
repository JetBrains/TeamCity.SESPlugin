package jetbrains.buildServer.sesPlugin.bounceHandler

import jetbrains.buildServer.sesPlugin.teamcity.util.UserSetProvider
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.users.SUser
import org.jmock.Expectations
import org.testng.annotations.Test

class BounceHandlerImplTest {

    @Test
    fun testHandleBounce() {
        mocking {
            val userBounceHandler1 = mock(UserBounceHandler::class.java, "userBounceHandler1")
            val userBounceHandler2 = mock(UserBounceHandler::class.java, "userBounceHandler2")
            val userSetProvider = mock(UserSetProvider::class.java)
            val user = mock(SUser::class.java)

            val handler = BounceHandlerImpl(userSetProvider, listOf(userBounceHandler1, userBounceHandler2))

            val mail = "mail"

            check {
                one(userSetProvider).users; will(Expectations.returnValue(setOf(user)));
                one(user).email; will(Expectations.returnValue(mail));
                one(userBounceHandler1).handleBounce(user);
                one(userBounceHandler2).handleBounce(user);
            }

            handler.handleBounce(mail)
        }

    }

    @Test
    fun testHandleBounce2Users() {
        mocking {
            val userBounceHandler = mock(UserBounceHandler::class.java)
            val userSetProvider = mock(UserSetProvider::class.java)
            val user1 = mock(SUser::class.java, "user1")
            val user2 = mock(SUser::class.java, "user2")

            val handler = BounceHandlerImpl(userSetProvider, listOf(userBounceHandler))

            val mail = "mail"

            check {
                one(userSetProvider).users; will(Expectations.returnValue(setOf(user1, user2)));
                one(user1).email; will(Expectations.returnValue(mail));
                one(user2).email; will(Expectations.returnValue(mail));
                one(userBounceHandler).handleBounce(user1);
                one(userBounceHandler).handleBounce(user2);
            }

            handler.handleBounce(mail)
        }

    }

    @Test
    fun testHandleBounceNoUsers() {
        mocking {
            val userBounceHandler = mock(UserBounceHandler::class.java)
            val userSetProvider = mock(UserSetProvider::class.java)
            val user = mock(SUser::class.java)

            val handler = BounceHandlerImpl(userSetProvider, listOf(userBounceHandler))

            val mail = "mail"

            check {
                one(userSetProvider).users; will(Expectations.returnValue(setOf(user)));
                one(user).email; will(Expectations.returnValue("otherMail"));
                never(userBounceHandler).handleBounce(user);
            }

            handler.handleBounce(mail)
        }

    }
}