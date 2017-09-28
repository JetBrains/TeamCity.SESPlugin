package jetbrains.buildServer.sesPlugin

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.sesPlugin.dataHolders.JsonRequestDataParser
import jetbrains.buildServer.sesPlugin.dataHolders.RequestData
import jetbrains.buildServer.sesPlugin.handlers.RequestHandlerFactory
import jetbrains.buildServer.sesPlugin.handlers.RequestHandlerFactoryImpl
import jetbrains.buildServer.sesPlugin.validator.RequestDataValidatorFactory
import jetbrains.buildServer.sesPlugin.validator.RequestDataValidatorFactoryImpl
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EndPoint(server: SBuildServer) : BaseController(server) {
    private val AMAZON_TYPE_HEADER = "x-amz-sns-message-type"

    private val myRequestDataValidatorFactory: RequestDataValidatorFactory
        get() {
            return RequestDataValidatorFactoryImpl()
        }
    private val myRequestHandlerFactory: RequestHandlerFactory
        get() {
            return RequestHandlerFactoryImpl()
        }

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


