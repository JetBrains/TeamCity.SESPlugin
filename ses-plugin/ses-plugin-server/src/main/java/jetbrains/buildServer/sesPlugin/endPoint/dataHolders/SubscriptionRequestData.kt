package jetbrains.buildServer.sesPlugin.endPoint.dataHolders

interface SubscriptionRequestData : RequestData {
    val token: String
    val subscribeURL: String
}