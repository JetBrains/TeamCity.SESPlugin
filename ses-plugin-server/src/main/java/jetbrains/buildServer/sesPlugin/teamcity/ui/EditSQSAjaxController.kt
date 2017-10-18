package jetbrains.buildServer.sesPlugin.teamcity.ui

import com.google.gson.Gson
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequest
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequestData
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

        val parameter = request.getParameter("json")
        val jsonString = if (parameter != null) parameter else {
            modelAndView.model.put("result", AjaxRequestResult(false, null, "No data provided"))
            return modelAndView
        }

        val req = Gson().fromJson<AjaxRequestData>(jsonString, AjaxRequestData::class.java)

        val res = ajaxRequests.find {
            it.id == req.type
        }?.handle(req) ?: AjaxRequestResult(false, null, "Unknown action ${req.type}")


        modelAndView.model.put("result", res)
        return modelAndView
    }
}