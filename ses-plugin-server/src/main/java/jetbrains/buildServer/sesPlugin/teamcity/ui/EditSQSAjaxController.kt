package jetbrains.buildServer.sesPlugin.teamcity.ui

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequest
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequestData
import jetbrains.buildServer.sesPlugin.teamcity.ui.ajax.AjaxRequestResult
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.json.MappingJackson2JsonView
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

        val jsonString = request.getParameter("json") ?: return null

        val req = ObjectMapper().readValue<AjaxRequestData>(jsonString, AjaxRequestData::class.java)

        val res = ajaxRequests.find {
            it.id == req.type
        }?.handle(req) ?: AjaxRequestResult(false, null, "Unknown action ${req.type}")


        val jsonRes: JsonNode = ObjectMapper().valueToTree(res)
        return ModelAndView(MappingJackson2JsonView(), mapOf("res" to jsonRes))
    }
}