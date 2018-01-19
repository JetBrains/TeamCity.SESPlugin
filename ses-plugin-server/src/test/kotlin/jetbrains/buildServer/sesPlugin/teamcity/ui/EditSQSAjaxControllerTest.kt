package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.serverSide.auth.Permission
import jetbrains.buildServer.sesPlugin.data.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequest
import jetbrains.buildServer.sesPlugin.teamcity.util.SessionUserProvider
import jetbrains.buildServer.sesPlugin.util.*
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations.returnValue
import org.testng.annotations.Test
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EditSQSAjaxControllerTest {
    @Test
    fun testNoUser() {
        mocking {
            val request = mock(HttpServletRequest::class)
            val response = mock(HttpServletResponse::class)
            val provider = mock(SessionUserProvider::class)

            check {
                one(provider).getUser(request); will(returnValue(null))
            }

            val res = EditSQSAjaxController(emptyList(), mock(WebControllerManager::class), provider).doHandle(request, response)
            BDDAssertions.then(res.model).containsKey("result")
            BDDAssertions.then(res.model["result"]).isExactlyInstanceOf(AjaxRequestResult::class.java)
            val result = res.model["result"] as AjaxRequestResult
            BDDAssertions.then(result.successful).isFalse()
        }
    }

    @Test
    fun testNoPermission() {
        mocking {
            val request = mock(HttpServletRequest::class)
            val response = mock(HttpServletResponse::class)
            val provider = mock(SessionUserProvider::class)
            val user = mock(SUser::class)

            check {
                one(provider).getUser(request); will(returnValue(user))
                one(user).isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS); will(returnValue(false))
            }

            val res = EditSQSAjaxController(emptyList(), mock(WebControllerManager::class), provider).doHandle(request, response)
            BDDAssertions.then(res.model).containsKey("result")
            BDDAssertions.then(res.model["result"]).isExactlyInstanceOf(AjaxRequestResult::class.java)
            val result = res.model["result"] as AjaxRequestResult
            BDDAssertions.then(result.successful).isFalse()
        }
    }

    @Test
    fun testNoType() {
        mocking {
            val request = mock(HttpServletRequest::class)
            val response = mock(HttpServletResponse::class)
            val provider = mock(SessionUserProvider::class)
            val user = mock(SUser::class)

            check {
                one(provider).getUser(request); will(returnValue(user))
                one(user).isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS); will(returnValue(true))
                one(request).getParameter("type"); will(returnValue(null))
            }

            val res = EditSQSAjaxController(emptyList(), mock(WebControllerManager::class), provider).doHandle(request, response)
            BDDAssertions.then(res.model).containsKey("result")
            BDDAssertions.then(res.model["result"]).isExactlyInstanceOf(AjaxRequestResult::class.java)
            val result = res.model["result"] as AjaxRequestResult
            BDDAssertions.then(result.successful).isFalse()
        }
    }

    @Test
    fun testNoHandler() {
        mocking {
            val request = mock(HttpServletRequest::class)
            val response = mock(HttpServletResponse::class)
            val provider = mock(SessionUserProvider::class)
            val user = mock(SUser::class)

            check {
                one(provider).getUser(request); will(returnValue(user))
                one(user).isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS); will(returnValue(true))
                one(request).getParameter("type"); will(returnValue("no"))
            }

            val res = EditSQSAjaxController(emptyList(), mock(WebControllerManager::class), provider).doHandle(request, response)
            BDDAssertions.then(res.model).containsKey("result")
            BDDAssertions.then(res.model["result"]).isExactlyInstanceOf(AjaxRequestResult::class.java)
            val result = res.model["result"] as AjaxRequestResult
            BDDAssertions.then(result.successful).isFalse()
        }
    }

    @Test
    fun testInit() {
        mocking {
            val webControllerManager = mock(WebControllerManager::class)

            val controller = EditSQSAjaxController(emptyList(), webControllerManager, mock(SessionUserProvider::class))

            check {
                one(webControllerManager).registerController(EditSQSAjaxController.URL, controller)
            }

            controller.init()
        }
    }

    @Test
    fun test() {
        mocking {
            val request = mock(HttpServletRequest::class)
            val response = mock(HttpServletResponse::class)
            val provider = mock(SessionUserProvider::class)
            val user = mock(SUser::class)
            val ajaxRequest = mock(AjaxRequest::class)

            val type = "type"
            check {
                one(provider).getUser(request); will(returnValue(user))
                one(user).isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS); will(returnValue(true))
                one(request).getParameter("type"); will(returnValue(type))
                one(ajaxRequest).id; will(returnValue(type))
                one(request).parameterMap; will(returnValue(HashMap<String, String>()))
            }
            val result = AjaxRequestResult(true, "")
            invocation(AjaxRequest::handle) {
                on(ajaxRequest)
                will(returnValue(result))
            }

            val res = EditSQSAjaxController(listOf(ajaxRequest), mock(WebControllerManager::class), provider).doHandle(request, response)
            BDDAssertions.then(res.model).containsKey("result")
            BDDAssertions.then(res.model["result"]).isSameAs(result)
        }
    }
}