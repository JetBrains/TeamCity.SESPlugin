package jetbrains.buildServer.sesPlugin.endPoint.handlers

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.NotificationRequestData
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.SubscribeRequestData
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.UnsubscribeRequestData
import jetbrains.buildServer.util.HTTPRequestBuilder

class RequestHandlerFactoryImpl(private val myRequestHandler: HTTPRequestBuilder.RequestHandler) : RequestHandlerFactory {
    override fun getHandler(data: RequestData): RequestHandler {
        return when (data) {
            is SubscribeRequestData -> SubscribeRequestHandler(data, myRequestHandler)
            is UnsubscribeRequestData -> UnsubscribeRequestHandler(data)
            is NotificationRequestData -> NotificationRequestHandler(data)
            else -> throw IllegalArgumentException("Unknown request type " + data.type)
        }
    }
}