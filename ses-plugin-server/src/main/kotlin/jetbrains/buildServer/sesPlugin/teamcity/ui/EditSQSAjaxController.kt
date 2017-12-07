package jetbrains.buildServer.sesPlugin.teamcity.ui

import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.controllers.BasePropertiesBean
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequest
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequestResult
import jetbrains.buildServer.sesPlugin.teamcity.util.GsonView
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EditSQSAjaxController(private val ajaxRequests: List<AjaxRequest>,
                            private val webControllerManager: WebControllerManager) : BaseController() {

    private val URL = "/admin/editSQSParams.html"

    fun init() {
        webControllerManager.registerController(URL, this)
    }

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        // todo handle permissions
        val modelAndView = ModelAndView(GsonView())

        val res = doHandleInternal(request, response)

        modelAndView.model.put("result", res)

        return modelAndView
    }

    private fun doHandleInternal(request: HttpServletRequest, response: HttpServletResponse): AjaxRequestResult {
        val type = request.getParameter("type") ?: return AjaxRequestResult(false, "No action type provided")

        return ajaxRequests.find {
            it.id == type
        }?.let {
            val bean = BasePropertiesBean(HashMap())
            PluginPropertiesUtil.bindPropertiesFromRequest(request, bean, true)

            return@let it.handle(bean)
        } ?: AjaxRequestResult(false, "Unknown action $type")
    }
}