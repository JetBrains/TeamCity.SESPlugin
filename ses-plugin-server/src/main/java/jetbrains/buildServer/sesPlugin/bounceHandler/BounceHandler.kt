package jetbrains.buildServer.sesPlugin.bounceHandler

interface BounceHandler {
    fun handleBounce(mail: String)
}