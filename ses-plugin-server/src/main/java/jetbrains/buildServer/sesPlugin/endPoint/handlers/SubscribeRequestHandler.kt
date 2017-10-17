package jetbrains.buildServer.sesPlugin.endPoint.handlers

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.SubscribeRequestData
import jetbrains.buildServer.util.HTTPRequestBuilder

class SubscribeRequestHandler(private val myData: SubscribeRequestData, private val myRequestHandler: HTTPRequestBuilder.RequestHandler) : RequestHandler {
    val myLogger: Logger = Logger.getInstance(SubscribeRequestHandler::javaClass.name)

    override fun handle() {
        val request = HTTPRequestBuilder(myData.subscribeURL)
                .withMethod("GET")
                .onException(this::onException)
                .onErrorResponse(this::onErrorResponse)
                .onSuccess(this::onSuccess)
                .build()

        myRequestHandler.doRequest(request)
    }

    private fun onException(ex: java.lang.Exception) {
        myLogger.error("Subscribe request for $myData got exception", ex)
    }

    private fun onErrorResponse(code: Int, data: String) {
        myLogger.debug("Subscribe request for $myData returned errorcode $code and $data")
    }

    private fun onSuccess(data: String) {
        myLogger.debug("Subscribe request for $myData returned $data")
        // todo Process the return value to ensure the endpoint is subscribed
    }
}