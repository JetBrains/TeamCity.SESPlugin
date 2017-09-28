package jetbrains.buildServer.sesPlugin.dataHolders

interface SubscriptionRequestData : RequestData {
    val token: String
    val subscribeURL: String
}