package jetbrains.buildServer.sesPlugin.bounceHandler

/**
 * Interface handling bounce events (hard ones only)
 */
interface BounceHandler {
    fun handleBounce(mail: String)
}