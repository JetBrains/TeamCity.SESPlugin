/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.bounceHandler.BounceHandler
import jetbrains.buildServer.sesPlugin.data.Recipient
import jetbrains.buildServer.sesPlugin.data.SESComplaintNotification
import jetbrains.buildServer.sesPlugin.util.*
import org.assertj.core.api.BDDAssertions.then
import org.hamcrest.CustomMatcher
import org.jmock.Expectations.returnValue
import org.jmock.Mockery
import org.testng.annotations.Test

class ComplaintMessageHandlerTest {
    @Test
    fun testAccept() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            then(handler(bounceHandler).accepts("asdf")).isFalse()
            then(handler(bounceHandler).accepts("Complaint")).isTrue()
        }
    }

    @Test
    fun testHandle() {
        mocking {
            val bounceHandler = mock(BounceHandler::class)
            val data = mock(SESComplaintNotification::class)
            check {
                one(data).getComplainedRecipients(); will(returnValue(sequenceOf(Recipient("mail", "no", "bad", "code"))))
            }
            invocation(BounceHandler::handleBounces) {
                on(bounceHandler)
                with(object : CustomMatcher<Array<Any>>("") {
                    override fun matches(p0: Any?): Boolean {
                        val a = p0 as Array<Any>
                        return (a[0] as Sequence<String>).first() == "mail"
                    }
                })
            }

            handler(bounceHandler).handle(data)
        }
    }

    private fun Mockery.handler(bounceHandler: BounceHandler = mock(BounceHandler::class)) = ComplaintMessageHandler(bounceHandler)
}