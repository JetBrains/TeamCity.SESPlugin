package jetbrains.buildServer.sesPlugin.handlers

import jetbrains.buildServer.sesPlugin.dataHolders.RequestData

interface RequestHandlerFactory {
    fun getHandler(data: RequestData): RequestHandler
}