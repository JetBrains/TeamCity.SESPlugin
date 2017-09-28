package jetbrains.buildServer.sesPlugin.handlers

import jetbrains.buildServer.sesPlugin.dataHolders.SubscribeRequestData
import jetbrains.buildServer.util.HTTPRequestBuilder

class SubscribeRequestHandler(private val data: SubscribeRequestData) : RequestHandler {
    // todo spring wiring
    lateinit var requestHandler: HTTPRequestBuilder.RequestHandler

    override fun handle() {
        val request = HTTPRequestBuilder(data.subscribeURL)
                .withMethod("GET")
                .onException(this::onException)
                .onErrorResponse(this::onErrorResponse)
                .onSuccess(this::onSuccess)
                .build()

        requestHandler.doRequest(request)
    }

    private fun onException(ex: java.lang.Exception) {

    }

    private fun onErrorResponse(code: Int, data: String) {

    }

    private fun onSuccess(data: String) {
        // todo Process the return value to ensure the endpoint is subscribed
    }
}