package jetbrains.buildServer.sesPlugin.endPoint.handlers

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData

interface RequestHandlerFactory {
    fun getHandler(data: RequestData): RequestHandler
}