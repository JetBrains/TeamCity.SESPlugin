package jetbrains.buildServer.sesPlugin.handlers

import jetbrains.buildServer.sesPlugin.dataHolders.NotificationRequestData
import jetbrains.buildServer.sesPlugin.dataHolders.RequestData
import jetbrains.buildServer.sesPlugin.dataHolders.SubscribeRequestData
import jetbrains.buildServer.sesPlugin.dataHolders.UnsubscribeRequestData

class RequestHandlerFactoryImpl : RequestHandlerFactory {
    override fun getHandler(data: RequestData): RequestHandler {
        return when (data) {
            is SubscribeRequestData -> SubscribeRequestHandler(data)
            is UnsubscribeRequestData -> UnsubscribeRequestHandler(data)
            is NotificationRequestData -> NotificationRequestHandler(data)
            else -> throw IllegalArgumentException("Unknown request type " + data.type)
        }
    }
}