package jetbrains.buildServer.sesPlugin.endPoint

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.JsonRequestDataParser
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData
import jetbrains.buildServer.sesPlugin.endPoint.handlers.RequestHandlerFactory
import jetbrains.buildServer.sesPlugin.endPoint.validator.RequestDataValidatorFactory
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EndPoint(server: SBuildServer,
               webControllerManager: WebControllerManager,
               private val myRequestDataValidatorFactory: RequestDataValidatorFactory,
               private val myRequestHandlerFactory: RequestHandlerFactory) : BaseController(server) {

    init {
        webControllerManager.registerController("ses-endpoint", this)
    }

    private val AMAZON_TYPE_HEADER = "x-amz-sns-message-type"

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        if (!acceptedContentLength(request.contentLength)) {
            return null
        }

        val inputStream = request.inputStream

        val data: RequestData
        try {
            val jsonResponseDataParser = JsonRequestDataParser()
            data = jsonResponseDataParser.parse(request.getHeader(AMAZON_TYPE_HEADER), inputStream)
        } catch (ex: JsonSyntaxException) {
            return null
        } catch (ex: JsonIOException) {
            return null
        }

        val validator = myRequestDataValidatorFactory.getValidator(data)
        val validate = validator.validate(data)

        validate.thenAccept {
            if (it.isCorrect) {
                val handler = myRequestHandlerFactory.getHandler(data)
                handler.handle()
            } else {

            }
        }

        return null
    }

    private fun acceptedContentLength(contentLength: Int): Boolean {
        return true
    }
}


